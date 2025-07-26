package com.sparta.user_service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
	@JsonProperty("role")
	private final RoleType role;

	public Role(RoleType role) {
		this.role = role;
	}

	public RoleType getRole() {
		return role;
	}
}
