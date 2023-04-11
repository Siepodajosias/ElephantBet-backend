package com.eburtis.ElephantBet.presentation.vo;

public class AuthVo {
	private String username;
	private String password;

	public AuthVo() {
	}

	public AuthVo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
