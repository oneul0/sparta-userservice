package com.sparta.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sparta.user_service.dto.LoginRequestDto;
import com.sparta.user_service.dto.LoginResponseDto;
import com.sparta.user_service.dto.SignUpRequestDto;
import com.sparta.user_service.dto.SignUpResponseDto;
import com.sparta.user_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "회원 가입, 로그인 및 관리자 권한 부여 API")
public class UserController {
	private final UserService userService;

	@Operation(summary = "회원 가입", description = "일반 사용자 회원 가입을 수행합니다.")
	@ApiResponse(responseCode = "200", description = "회원 가입 성공")
	@PostMapping("/signup")
	public ResponseEntity<?> signup(
		@Parameter(description = "회원 가입 요청 정보", required = true)
		@RequestBody SignUpRequestDto request) {
		return ResponseEntity.ok(userService.signup(request));
	}

	@Operation(summary = "로그인", description = "일반 사용자 로그인 후 JWT 토큰을 발급합니다.")
	@ApiResponse(responseCode = "200", description = "로그인 성공, 토큰 발급",
		content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
	@PostMapping("/login")
	public ResponseEntity<?> login(
		@Parameter(description = "로그인 요청 정보", required = true)
		@RequestBody LoginRequestDto request) {
		String token = userService.login(request);
		return ResponseEntity.ok(new LoginResponseDto(token));
	}

	@Operation(summary = "관리자 회원 가입", description = "관리자 계정 회원 가입을 수행합니다.")
	@ApiResponse(responseCode = "200", description = "관리자 회원 가입 성공")
	@PostMapping("/admin/signup")
	public ResponseEntity<?> adminSignup(
		@Parameter(description = "관리자 회원 가입 요청 정보", required = true)
		@RequestBody SignUpRequestDto request) {
		return ResponseEntity.ok(userService.adminSignup(request));
	}

	@Operation(summary = "관리자 로그인", description = "관리자 로그인 후 JWT 토큰을 발급합니다.")
	@ApiResponse(responseCode = "200", description = "관리자 로그인 성공, 토큰 발급",
		content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
	@PostMapping("/admin/login")
	public ResponseEntity<?> adminLogin(
		@Parameter(description = "관리자 로그인 요청 정보", required = true)
		@RequestBody LoginRequestDto request) {
		String token = userService.login(request);
		return ResponseEntity.ok(new LoginResponseDto(token));
	}

	@Operation(summary = "관리자 권한 부여", description = "특정 사용자에게 관리자 권한을 부여합니다.")
	@ApiResponse(responseCode = "200", description = "관리자 권한 부여 성공",
		content = @Content(schema = @Schema(implementation = SignUpResponseDto.class)))
	@PatchMapping("/admin/users/{userId}/roles")
	public ResponseEntity<SignUpResponseDto> grantAdmin(
		@Parameter(description = "권한을 부여할 사용자 ID", required = true)
		@PathVariable Long userId) {
		SignUpResponseDto response = userService.grantAdminRole(userId);
		return ResponseEntity.ok(response);
	}
}
