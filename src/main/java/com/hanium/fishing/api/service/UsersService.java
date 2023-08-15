package com.hanium.fishing.api.service;

import com.hanium.fishing.api.domain.entity.RefreshToken;
import com.hanium.fishing.api.domain.entity.Users;
import com.hanium.fishing.api.domain.repository.UsersRepository;
import com.hanium.fishing.api.dto.request.JoinRequestDto;
import com.hanium.fishing.api.dto.response.TokenResponseDto;
import com.hanium.fishing.exception.CustomException;
import com.hanium.fishing.exception.ErrorCode;
import com.hanium.fishing.utils.JwtTokenUtil;
import com.hanium.fishing.api.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String key;

    public void checkDuplicate(String userId) {
        if(usersRepository.findByUserId(userId).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_USERID);
        }
    }

    public String join(JoinRequestDto joinDto) {

        usersRepository.save(JoinRequestDto.toEntity(joinDto.getUserId(), encoder.encode(joinDto.getPassword()), joinDto.getNickName(), joinDto.getProfileImageUrl()));
        return joinDto.getUserId() +  "님이 성공적으로 회원가입되었습니다.";
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

    // 나중에 Redis로 저장하는거 구현하기
    public TokenResponseDto reissue(String refreshToken) {
        refreshToken = refreshToken.split(" ")[1];

        if(JwtTokenUtil.isExpired(refreshToken, key)) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
        RefreshToken inputToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
        RefreshToken token = refreshTokenRepository.findByUserId(inputToken.getUserId()).orElseThrow(()-> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        TokenResponseDto newToken = JwtTokenUtil.createAllToken(inputToken.getUserId(), key);
        refreshTokenRepository.save(token.updateToken(newToken.getRefreshToken()));
        return TokenResponseDto.builder()
                .accessToken(newToken.getAccessToken())
                .refreshToken(newToken.getRefreshToken())
                .build();
    }
}
