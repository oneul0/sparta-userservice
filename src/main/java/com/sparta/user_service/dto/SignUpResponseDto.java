package com.sparta.user_service.dto;

import java.util.List;

import com.sparta.user_service.domain.Role;

public record SignUpResponseDto(String username, String nickname, List<Role> roles) {
}
