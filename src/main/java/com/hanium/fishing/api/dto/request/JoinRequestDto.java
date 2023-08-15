package com.hanium.fishing.api.dto.request;

import com.hanium.fishing.api.domain.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinRequestDto {
    private String userId;
    private String password;
    private String nickName;
    private String profileImageUrl;

    public static Users toEntity(String userId, String password, String nickName, String profileImageUrl) {
        return Users.builder()
                .userId(userId)
                .password(password)
                .nickName(nickName)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
