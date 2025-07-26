package com.sparta.user_service.domain;

import java.util.ArrayList;
import java.util.List;

public class User {
	private final Long id;
	private final String username;
	private final String password;
	private final String nickname;
	private final List<Role> roles = new ArrayList<>();

	public User(Long id, String username, String password, String nickname) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.roles.add(new Role(RoleType.USER));
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}
}

