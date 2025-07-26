package com.sparta.user_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.user_service.domain.User;
import com.sparta.user_service.dto.SignUpRequestDto;
import com.sparta.user_service.dto.SignUpResponseDto;
import com.sparta.user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private static UserRepository userRepository;
	private static PasswordEncoder passwordEncoder;

	public SignUpResponseDto signup(SignUpRequestDto request) {
		//todo: 에러 검사 추가
		User user = new User(
			request.username(),
			passwordEncoder.encode(request.password()),
			request.nickname()
		);

		//todo: 인메모리 저장 로직 추가
		return new SignUpResponseDto(
			user.getUsername(),
			user.getNickname(),
			user.getRoles()
		);
	}

}
