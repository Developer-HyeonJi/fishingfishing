package com.hanium.fishing.api.controller;

import com.hanium.fishing.api.dto.request.JoinRequestDto;
import com.hanium.fishing.api.dto.request.LoginRequestDto;
import com.hanium.fishing.api.dto.response.TokenResponseDto;
import com.hanium.fishing.api.service.UsersService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;


    @ApiOperation(value = "중복 확인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 409, message = "이미 존재하는 아이디입니다")
    })

    @ApiOperation(value = "회원 정보 조회")
    @GetMapping("/check/{user-id}")
    public ResponseEntity<String> checkDuplicate(@PathVariable(name = "user-id") String userId) {

        usersService.checkDuplicate(userId);
        return ResponseEntity.ok().body("중복되는 아이디가 없습니다.");
    }

    @ApiOperation(value = "회원 가입")
    @PostMapping(value = "/join", produces = "application/text;charset = utf-8")
    public ResponseEntity<String> join(@RequestBody JoinRequestDto joinDto) {
        return ResponseEntity.ok().body(usersService.join(joinDto));
    }


    @ApiOperation(value = "로그인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "아이디를 찾을 수 없습니다"),
            @ApiResponse(code = 401, message = "비밀번호가 일치하지 않습니다")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginDto) {

        TokenResponseDto token = usersService.login(loginDto.getUserId(), loginDto.getPassword());
        return ResponseEntity.ok().body(token);
    }

    @ApiOperation(value = "토큰 재발급")
    @GetMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestHeader("RefreshToken") String refreshToken) {
        return ResponseEntity.ok().body(usersService.reissue(refreshToken));
    }

    @ApiOperation(value = "회원 정보 조회")
    @GetMapping("/users/{user-id}") // 추가
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable(name = "user-id") String userId) {
        // UsersService를 사용하여 회원 정보 조회
        UserResponseDto userInfo = usersService.getUserInfo(userId);
        return ResponseEntity.ok().body(userInfo);
    }

    @ApiOperation(value = "회원 정보 수정")
    @PutMapping("/users/{user-id}") // 추가
    public ResponseEntity<String> updateUser(@PathVariable(name = "user-id") String userId, @RequestBody UpdateUserRequestDto updateUserDto) {
        // UsersService를 사용하여 회원 정보 수정
        String resultMessage = usersService.updateUser(userId, updateUserDto);
        return ResponseEntity.ok().body(resultMessage);
    }
}
