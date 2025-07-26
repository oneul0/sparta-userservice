package com.sparta.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.user_service.dto.LoginRequestDto;
import com.sparta.user_service.dto.LoginResponseDto;
import com.sparta.user_service.dto.SignUpRequestDto;
import com.sparta.user_service.dto.SignUpResponseDto;
import com.sparta.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private static UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SignUpRequestDto request) {
		return ResponseEntity.ok(userService.signup(request));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
		String token = userService.login(request);
		return ResponseEntity.ok(new LoginResponseDto(token));
	}

	@PostMapping("/admin/signup")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> adminSignup(@RequestBody SignUpRequestDto request) {
		return ResponseEntity.ok(userService.adminSignup(request));
	}

	@PostMapping("/admin/login")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> adminLogin(@RequestBody LoginRequestDto request) {
		String token = userService.login(request);
		return ResponseEntity.ok(new LoginResponseDto(token));
	}
}
