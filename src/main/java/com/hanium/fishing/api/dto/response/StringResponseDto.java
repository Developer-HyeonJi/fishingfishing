package com.hanium.fishing.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StringResponseDto {
    private String result;

    public static StringResponseDto of(String result) {
        return StringResponseDto.builder()
                .result(result)
                .build();
    }
}