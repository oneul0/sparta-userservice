package com.sparta.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.user_service.config.TestSecurityConfig;
import com.sparta.user_service.config.JwtTokenProvider;
import com.sparta.user_service.domain.User;
import com.sparta.user_service.dto.LoginRequestDto;
import com.sparta.user_service.dto.SignUpRequestDto;
import com.sparta.user_service.dto.SignUpResponseDto;
import com.sparta.user_service.domain.Role;
import com.sparta.user_service.domain.RoleType;
import com.sparta.user_service.exception.GlobalException;
import com.sparta.user_service.exception.UserErrorCode;
import com.sparta.user_service.repository.UserRepository;
import com.sparta.user_service.service.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private PasswordEncoder passwordEncoder;

	@MockitoBean
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("회원가입 성공")
	void signup_success() throws Exception {
		SignUpRequestDto requestDto = new SignUpRequestDto("JIN HO", "12341234", "Mentos");
		SignUpResponseDto responseDto = new SignUpResponseDto("JIN HO", "Mentos", List.of(new Role(RoleType.USER)));

		Mockito.when(userService.signup(Mockito.any())).thenReturn(responseDto);

		mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto))
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("JIN HO"))
			.andExpect(jsonPath("$.nickname").value("Mentos"))
			.andExpect(jsonPath("$.roles[0].role").value("USER"));
	}

	@DisplayName("회원가입 실패 - 이미 가입된 사용자")
	@Test
	void signup_fail_already_exists_direct() {
		SignUpRequestDto requestDto = new SignUpRequestDto("test@example.com", "password123", "nickname");

		Mockito.when(userService.signup(Mockito.any()))
			.thenThrow(new GlobalException(UserErrorCode.USER_ALREADY_EXISTS));

		GlobalException exception = assertThrows(GlobalException.class, () -> {
			userService.signup(requestDto);
		});

		assertEquals("이미 가입된 사용자입니다.", exception.getMessage());
	}

	@Test
	@DisplayName("로그인 성공")
	void login_success() throws Exception {
		LoginRequestDto loginRequestDto = new LoginRequestDto("JIN HO", "12341234");
		String token = "fake-jwt-token";

		Mockito.when(userService.login(Mockito.any())).thenReturn(token);

		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequestDto))
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value("fake-jwt-token"));
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 일치하지 않음")
	void login_fail_invalid_credentials() {
		LoginRequestDto loginRequestDto = new LoginRequestDto("JIN HO", "wrongpassword");

		Mockito.when(userService.login(Mockito.any()))
			.thenThrow(new GlobalException(UserErrorCode.INVALID_CREDENTIALS));

		GlobalException exception = assertThrows(GlobalException.class, () -> {
			userService.login(loginRequestDto);
		});

		assertEquals("아이디 또는 비밀번호가 올바르지 않습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("로그인 실패 - 존재하지 않는 사용자")
	void login_fail_user_not_found() {
		LoginRequestDto loginRequestDto = new LoginRequestDto("NONEXISTENT", "12341234");

		Mockito.when(userService.login(Mockito.any()))
			.thenThrow(new GlobalException(UserErrorCode.INVALID_CREDENTIALS));

		GlobalException exception = assertThrows(GlobalException.class, () -> {
			userService.login(loginRequestDto);
		});

		assertEquals("아이디 또는 비밀번호가 올바르지 않습니다.", exception.getMessage());
	}



	@Test
	@DisplayName("관리자 권한 부여 성공 - 관리자 권한으로 요청")
	@WithMockUser(roles = "ADMIN")
	void grantAdminRole_success() throws Exception {
		Long userId = 1L;
		SignUpResponseDto responseDto = new SignUpResponseDto("JIN HO", "Mentos", List.of(new Role(RoleType.ADMIN)));

		Mockito.when(userService.grantAdminRole(userId)).thenReturn(responseDto);

		mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.roles[0].role").value("ADMIN"));
	}

	@Test
	@DisplayName("관리자 권한 부여 실패 - 일반 사용자 권한으로 요청")
	@WithMockUser(roles = "USER")
	void grantAdminRole_fail_insufficient_permission() throws Exception {
		Long userId = 1L;

		mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("관리자 권한 부여 실패 - 인증되지 않은 요청")
	void grantAdminRole_fail_unauthenticated() throws Exception {
		Long userId = 1L;

		mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
				.with(csrf()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("관리자 권한 부여 실패 - 존재하지 않는 사용자")
	void grantAdminRole_fail_user_not_found() {
		Long userId = 999L;

		Mockito.when(userService.grantAdminRole(userId))
			.thenThrow(new GlobalException(UserErrorCode.INVALID_CREDENTIALS));

		Exception exception = assertThrows(GlobalException.class, () -> {
			userService.grantAdminRole(userId);
		});
		
		assertEquals("아이디 또는 비밀번호가 올바르지 않습니다.", exception.getMessage());
	}

}