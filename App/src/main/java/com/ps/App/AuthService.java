package com.ps.App;

public class AuthService {
	public String login(String url, String jsonInputString) {
		HttpClient client = new HttpClient();
		try {
			// Hacer la solicitud POST para obtener el token
			String postResponse = client.sendPost(url, jsonInputString);
			AuthResponse authResponse = AuthResponse.fromJson(postResponse);
			return authResponse.getToken();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}