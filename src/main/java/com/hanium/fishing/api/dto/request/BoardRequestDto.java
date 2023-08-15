package com.hanium.fishing.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequestDto {

    private String title;
    private String content;
    private String MainImageUrl;

}
