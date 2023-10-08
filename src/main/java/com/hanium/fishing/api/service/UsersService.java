package com.hanium.fishing.api.service;

import com.hanium.fishing.api.domain.entity.RefreshToken;
import com.hanium.fishing.api.domain.entity.Users;
import com.hanium.fishing.api.domain.repository.RefreshTokenRepository;
import com.hanium.fishing.api.domain.repository.UsersRepository;
import com.hanium.fishing.api.dto.request.JoinRequestDto;
import com.hanium.fishing.api.dto.request.UpdateUserRequestDto;
import com.hanium.fishing.api.dto.response.TokenResponseDto;
import com.hanium.fishing.api.dto.response.UserResponseDto;
import com.hanium.fishing.exception.CustomException;
import com.hanium.fishing.exception.ErrorCode;
import com.hanium.fishing.api.dto.response.StringResponseDto;
import com.hanium.fishing.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder encoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String key;

    public StringResponseDto checkDuplicate(String userId) {
        if(usersRepository.findByUserId(userId).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USERID);
        }
        return StringResponseDto.of("사용 가능한 아이디입니다.");
    }

    public String join(JoinRequestDto joinDto) {
        usersRepository.save(JoinRequestDto.toEntity(joinDto.getUserId(), encoder.encode(joinDto.getPassword()), joinDto.getNickName(), joinDto.getProfileImageUrl()));
        return joinDto.getUserId() + "님이 성공적으로 회원가입되었습니다.";
    }

    public TokenResponseDto login(String userId, String password) {
        Users user = usersRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.USERID_NOT_FOUND));
        if(!encoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        TokenResponseDto token = JwtTokenUtil.createAllToken(user.getUserId(), key);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(userId);

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(token.getRefreshToken()));
        } else {
            RefreshToken newRefreshToken = new RefreshToken(userId,token.getRefreshToken());
            refreshTokenRepository.save(newRefreshToken);
        }
        return TokenResponseDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public TokenResponseDto reissue(String refreshToken) {
        refreshToken = refreshToken.split(" ")[1];

        if (JwtTokenUtil.isExpired(refreshToken, key)) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        RefreshToken inputToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        RefreshToken token = refreshTokenRepository.findByUserId(inputToken.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        TokenResponseDto newToken = JwtTokenUtil.createAllToken(inputToken.getUserId(), key);
        refreshTokenRepository.save(token.updateToken(newToken.getRefreshToken()));

        return TokenResponseDto.builder()
                .accessToken(newToken.getAccessToken())
                .refreshToken(newToken.getRefreshToken())
                .build();
    }

    public Users getUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponseDto getUserInfo(String userId) {
        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserResponseDto.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    public String updateUser(String userId, UpdateUserRequestDto updateUserRequestDto) {
        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (updateUserRequestDto.getNickName() != null) {
            user.setNickName(updateUserRequestDto.getNickName());
        }

        if (updateUserRequestDto.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updateUserRequestDto.getProfileImageUrl());
        }

        usersRepository.save(user);

        return "회원 정보가 업데이트되었습니다.";
    }
}