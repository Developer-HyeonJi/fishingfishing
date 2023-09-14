package com.hanium.fishing.api.dto.response;

import com.hanium.fishing.api.domain.entity.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long userId;
    private String nickName;
    private String profileImageUrl;
    private String accessToken;
    private String refreshToken;

    public static UserResponseDto from(Users user, String accessToken, String refreshToken) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .profileImageUrl(user.getProfileImageUrl())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
