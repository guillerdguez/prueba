package com.tamscrap.dto;

public class AuthResponse {
	private String token;
	private ClienteResponseDTO user;

	public AuthResponse() {
	}

	public AuthResponse(String token, ClienteResponseDTO user) {
		this.token = token;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ClienteResponseDTO getUser() {
		return user;
	}

	public void setUser(ClienteResponseDTO user) {
		this.user = user;
	}
}
