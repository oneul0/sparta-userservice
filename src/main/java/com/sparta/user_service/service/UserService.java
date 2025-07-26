package com.sparta.user_service.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.user_service.config.JwtTokenProvider;
import com.sparta.user_service.domain.Role;
import com.sparta.user_service.domain.RoleType;
import com.sparta.user_service.domain.User;
import com.sparta.user_service.dto.LoginRequestDto;
import com.sparta.user_service.dto.SignUpRequestDto;
import com.sparta.user_service.dto.SignUpResponseDto;
import com.sparta.user_service.exception.GlobalException;
import com.sparta.user_service.exception.UserErrorCode;
import com.sparta.user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	public SignUpResponseDto signup(SignUpRequestDto request) {
		if (userRepository.existsByUsername(request.username())) {
			throw new GlobalException(UserErrorCode.USER_ALREADY_EXISTS);
		}

		User user = userRepository.save(
			request.username(),
			passwordEncoder.encode(request.password()),
			request.nickname(),
			RoleType.USER
		);

		return new SignUpResponseDto(
			user.getUsername(),
			user.getNickname(),
			user.getRoles()
		);
	}

	public SignUpResponseDto adminSignup(SignUpRequestDto request) {
		if (userRepository.existsByUsername(request.username())) {
			throw new GlobalException(UserErrorCode.USER_ALREADY_EXISTS);
		}

		User user = userRepository.save(
			request.username(),
			passwordEncoder.encode(request.password()),
			request.nickname(),
			RoleType.ADMIN
		);

		return new SignUpResponseDto(
			user.getUsername(),
			user.getNickname(),
			user.getRoles()
		);
	}

	public String login(LoginRequestDto request) {
		User user = userRepository.findByUsername(request.username())
			.orElseThrow(() -> new GlobalException(UserErrorCode.INVALID_CREDENTIALS));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new GlobalException(UserErrorCode.INVALID_CREDENTIALS);
		}

		return jwtTokenProvider.generateToken(user);
	}

	public SignUpResponseDto grantAdminRole(Long userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null ||
			authentication.getAuthorities().stream()
				.noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
			throw new GlobalException(UserErrorCode.ACCESS_DENIED);
		}

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new GlobalException(UserErrorCode.INVALID_CREDENTIALS));

		boolean hasAdmin = user.getRoles().stream()
			.anyMatch(role -> role.getRole() == RoleType.ADMIN);

		if (!hasAdmin) {
			user.addRole(new Role(RoleType.ADMIN));
		}

		return new SignUpResponseDto(
			user.getUsername(),
			user.getNickname(),
			user.getRoles()
		);
	}
}
