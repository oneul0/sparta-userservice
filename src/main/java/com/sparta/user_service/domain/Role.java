package com.sparta.user_service.domain;

public class Role {
	private final RoleType role;

	public Role(RoleType role) {
		this.role = role;
	}

	public RoleType getRole() {
		return role;
	}
}
