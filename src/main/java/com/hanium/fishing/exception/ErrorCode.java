package com.hanium.fishing.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),
    USERID_NOT_FOUND(NOT_FOUND, "아이디를 찾을 수 없습니다"),
    INVALID_PASSWORD(UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),
    INVALID_JWT_TOKEN(UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다"),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다"),
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다"),
    DUPLICATE_USERID(CONFLICT, "이미 존재하는 아이디입니다");


    private final HttpStatus httpStatus;
    private final String message;
}
