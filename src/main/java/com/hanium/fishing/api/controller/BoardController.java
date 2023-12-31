package com.hanium.fishing.api.controller;

import com.hanium.fishing.api.dto.request.BoardRequestDto;
import com.hanium.fishing.api.dto.response.AllBoardListResponseDto;
import com.hanium.fishing.api.dto.response.StringResponseDto;
import com.hanium.fishing.api.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.hanium.fishing.api.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class BoardController {

    private final BoardService boardService;
    private S3Service s3Service;

    @ApiOperation(value = "게시판 생성")
    @PostMapping("/fishing/board")
    public ResponseEntity<Long> createBoard(BoardRequestDto boardRequestDto) {
        Long newBoardId = boardService.createBoard(boardRequestDto);
        return ResponseEntity.ok().body(newBoardId);
    }


    @ApiOperation(value = "게시판 조회")
    @GetMapping({"/fishing/board/all"})
    public ResponseEntity<List<AllBoardListResponseDto>> getAllBoards(Authentication authentication) {
        List<AllBoardListResponseDto> allBoards = this.boardService.getAllBoards();
        return ResponseEntity.ok().body(allBoards);
    }

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/fishing/url")
    public ResponseEntity<StringResponseDto> image(@RequestPart MultipartFile image) {
        String imageUrl = s3Service.uploadImage(image);
        return ResponseEntity.ok().body(StringResponseDto.of(imageUrl));
    }
}
