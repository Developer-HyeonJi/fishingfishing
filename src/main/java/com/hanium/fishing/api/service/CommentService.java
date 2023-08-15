package com.hanium.fishing.api.service;

import com.hanium.fishing.api.dto.request.CommentRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentService {


    public ResponseEntity<String> addCommentToBoard(String string, CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok().body("댓글이 등록되었습니다.");
    }
}
