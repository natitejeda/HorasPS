package com.ps.App;

import org.json.JSONObject;

public class AuthResponse {
	private String token;

	public AuthResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public static AuthResponse fromJson(String jsonString) {
		JSONObject jsonObject = new JSONObject(jsonString);
		String token = jsonObject.getString("token");
		return new AuthResponse(token);
	}
}
