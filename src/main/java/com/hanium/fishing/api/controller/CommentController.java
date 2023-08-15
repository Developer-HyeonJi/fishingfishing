package com.hanium.fishing.api.controller;

import com.hanium.fishing.api.dto.request.CommentRequestDto;
import com.hanium.fishing.api.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @ApiOperation(value = "Add a comment to a board")
    @PostMapping("/fishing/boards/{boardId}/comments")
    public ResponseEntity<ResponseEntity<String>> addCommentToBoard(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto commentRequestDto) {
        ResponseEntity<String> commentResponseDto = commentService.addCommentToBoard(
                String.valueOf(boardId), commentRequestDto);
        return ResponseEntity.ok().body(commentResponseDto);
    }
}