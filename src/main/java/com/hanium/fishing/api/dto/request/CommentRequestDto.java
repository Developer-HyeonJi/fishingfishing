package com.hanium.fishing.api.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentRequestDto {
    private Long boardId;
    private String commentContent;
}