package com.hanium.fishing.api.dto.response;

import com.hanium.fishing.api.domain.entity.Board;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto  {
    private Long id;
    private String title;
    private String content;
    private String mainImage;

    public static BoardDto from (Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());
        boardDto.setTitle(board.getTitle());
        boardDto.setContent(board.getContent());
        boardDto.setMainImage(board.getMainImage());

        return boardDto;
    }
}
