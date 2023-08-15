package com.hanium.fishing.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllBoardListResponseDto {
    private Long BoardId;
    private String title;
    private String mainImageUrl;

    public static AllBoardListResponseDto of(Long BoardId, String title, String mainImageUrl) {
        return AllBoardListResponseDto.builder()
                .BoardId(BoardId)
                .title(title)
                .mainImageUrl(mainImageUrl)
                .build();
    }
}
