package com.hanium.fishing.api.dto.response;

import com.hanium.fishing.api.domain.entity.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsersResponseDto {
    private Long userId;
    private String nickName;
    private String profileImageUrl;

    public static UsersResponseDto from(Users user) {
        return UsersResponseDto.builder()
                .userId(user.getId())
                .nickName(user.getNickName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
