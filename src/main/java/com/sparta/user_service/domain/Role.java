package com.sparta.user_service.domain;

public enum Role {
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private final String authority;

	Role(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}

	public String getRole() {
		return name();
	}
}
