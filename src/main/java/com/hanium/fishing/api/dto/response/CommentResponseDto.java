package com.hanium.fishing.api.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class CommentResponseDto {
    private Long commentId;
    private Long boardId;
    private String commentContent;
    private String createdDate;

    public CommentResponseDto(Long commentId, String commentContent) {
        this.commentId = commentId;
        this.commentContent = commentContent;
    }
}