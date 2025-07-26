package com.sparta.user_service.domain;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String username;
	private String password;
	private String nickname;
	private List<Role> roles;

	public User(String username, String password, String nickname) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.roles = new ArrayList<>();
		this.roles.add(Role.USER); // enum 사용
	}

	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getNickname() { return nickname; }
	public List<Role> getRoles() { return roles; }
	public void addRole(Role role) { this.roles.add(role); }
}
