package com.sparta.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
