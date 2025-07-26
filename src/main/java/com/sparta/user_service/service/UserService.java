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
		if (userRepository.existsByUsername(request.username())) {
			throw new DuplicateUserException("이미 가입된 사용자입니다.");
		}

		User user = userRepository.save(
			request.username(),
			passwordEncoder.encode(request.password()),
			request.nickname()
		);

		return new SignUpResponseDto(
			user.getUsername(),
			user.getNickname(),
			user.getRoles()
		);
	}

}
