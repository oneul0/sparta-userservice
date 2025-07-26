package com.sparta.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.user_service.dto.SignUpRequestDto;
import com.sparta.user_service.dto.SignUpResponseDto;
import com.sparta.user_service.service.UserService;

@RestController
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signup(@RequestBody SignUpRequestDto request) {
		return null;
	}
}
