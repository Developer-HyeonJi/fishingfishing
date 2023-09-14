package com.hanium.fishing.api.service;

import com.hanium.fishing.api.domain.entity.RefreshToken;
import com.hanium.fishing.api.domain.entity.Users;
import com.hanium.fishing.api.domain.repository.UsersRepository;
import com.hanium.fishing.api.dto.request.JoinRequestDto;
import com.hanium.fishing.api.dto.request.LoginRequestDto;
import com.hanium.fishing.api.dto.request.UpdateUserRequestDto;
import com.hanium.fishing.api.dto.response.TokenResponseDto;
import com.hanium.fishing.api.dto.response.UserResponseDto;
import com.hanium.fishing.exception.CustomException;
import com.hanium.fishing.exception.ErrorCode;
import com.hanium.fishing.utils.JwtTokenUtil;
import com.hanium.fishing.api.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // 멀티파트 파일 임포트 추가

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
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String key;

    public void checkDuplicate(String userId) {
        if (usersRepository.findByUserId(userId).isPresent( )) {
            throw new CustomException(ErrorCode.DUPLICATE_USERID);
        }
    }

    public String join(JoinRequestDto joinDto, MultipartFile profileImage) { // 멀티파트 파일 추가
        // JoinRequestDto에서 프로필 이미지 정보를 추출하고 사용할 수 있습니다.
        String profileImageUrl = processProfileImage(profileImage); // 프로필 이미지 처리 메서드 호출
        Users user = JoinRequestDto.toEntity(joinDto.getUserId( ), encoder.encode(joinDto.getPassword( )), joinDto.getNickName( ), profileImageUrl);
        usersRepository.save(user);
        return joinDto.getUserId( ) + "님이 성공적으로 회원가입되었습니다.";
    }

    // 프로필 이미지 처리 메서드 추가
    private String processProfileImage(MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty( )) {
            return null; // 업로드된 이미지가 없으면 null 반환
        }

        try {
            // 실제로 이미지를 저장하고, 저장된 이미지의 URL을 얻어올 수 있는 로직을 구현하세요.
            // 파일 시스템에 저장하는 예제 (프로젝트의 요구에 따라 다르게 구현 가능)
            String originalFilename = profileImage.getOriginalFilename( );
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID( ).toString( ) + fileExtension; // 새로운 파일 이름 생성

            // 프로필 이미지를 저장할 경로 설정 (프로젝트에 맞게 설정하세요)
            String uploadDirectory = "path/to/upload/directory";
            Path filePath = Paths.get(uploadDirectory, newFilename);

            // 파일 저장
            Files.write(filePath, profileImage.getBytes( ));

            // 파일 URL을 얻어올 수 있는 방식에 따라 URL을 생성 또는 반환하세요.
            // 예를 들어, 웹 애플리케이션의 경우 파일 URL을 서버 내부 경로에서 외부 URL로 변환해야 할 수 있습니다.
            String baseUrl = "http://example.com/images"; // 예시 URL
            String fileUrl = baseUrl + "/" + newFilename; // 파일의 외부 URL

            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace( ); // 오류 처리 필요
            return null;
        }
    }


    public TokenResponseDto login(String userId, String password) {
        Users user = usersRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.USERID_NOT_FOUND));
        if (!encoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        TokenResponseDto token = JwtTokenUtil.createAllToken(user.getUserId(), key);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(userId);

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(token.getRefreshToken()));
        } else {
            RefreshToken newRefreshToken = new RefreshToken(userId, token.getRefreshToken());
            refreshTokenRepository.save(newRefreshToken);
        }

        return token;
    }


    public TokenResponseDto reissue(String refreshToken) {
        refreshToken = refreshToken.split(" ")[1];

        if (JwtTokenUtil.isExpired(refreshToken, key)) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
        RefreshToken inputToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
        RefreshToken token = refreshTokenRepository.findByUserId(inputToken.getUserId( )).orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        TokenResponseDto newToken = JwtTokenUtil.createAllToken(inputToken.getUserId( ), key);
        refreshTokenRepository.save(token.updateToken(newToken.getRefreshToken( )));
        return TokenResponseDto.builder( )
                .accessToken(newToken.getAccessToken( ))
                .refreshToken(newToken.getRefreshToken( ))
                .build( );
    }

    public Users getUserById(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public UserResponseDto getUserInfo(String userId) {
        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserResponseDto.builder( )
                .userId(user.getId( ))
                .nickName(user.getNickName( ))
                .profileImageUrl(user.getProfileImageUrl( ))
                .build( );
    }

    public String updateUser(String userId, UpdateUserRequestDto updateUserRequestDto, MultipartFile profileImage) {
        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (updateUserRequestDto.getNickName( ) != null) {
            user.setNickName(updateUserRequestDto.getNickName( ));
        }

        if (profileImage != null) {
            String newProfileImageUrl = processProfileImage(profileImage);
            user.setProfileImageUrl(newProfileImageUrl);
        }

        usersRepository.save(user);

        return "회원 정보가 업데이트되었습니다.";
    }
}
