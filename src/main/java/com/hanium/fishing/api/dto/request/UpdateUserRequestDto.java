package com.hanium.fishing.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {
    private String nickName;
    private String profileImageUrl;
}
