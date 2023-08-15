package com.hanium.fishing.api.service;

import com.hanium.fishing.api.domain.entity.Board;
import com.hanium.fishing.api.dto.request.BoardRequestDto;
import com.hanium.fishing.api.domain.repository.BoardRepository;
import com.hanium.fishing.api.dto.response.AllBoardListResponseDto;
import com.hanium.fishing.api.dto.response.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Long createBoard(BoardRequestDto boardRequestDto) {
        Board newBoard = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .mainImage(boardRequestDto.getMainImageUrl())
                .build();

        Board savedBoard = boardRepository.save(newBoard);
        return savedBoard.getId(); // Return the newly created board's ID
    }


    public List<AllBoardListResponseDto> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(board -> AllBoardListResponseDto.of(board.getId(), board.getTitle(), board.getMainImage()))
                .collect(Collectors.toList());
    }
}



