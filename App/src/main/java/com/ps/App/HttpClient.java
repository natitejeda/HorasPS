package com.ps.App;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class HttpClient {
	// Ejecuta una solicitud POST y devuelve la respuesta como una cadena.
	public String sendPost(String url, String jsonInputString) throws Exception {
		URI uri = new URI(url);
	    URL endpointUrl = uri.toURL(); // Convierte URI a URL
	    HttpURLConnection con = (HttpURLConnection) endpointUrl.openConnection();
	    con.setRequestMethod("POST");
	    con.setRequestProperty("Content-Type", "application/json");
	    con.setDoOutput(true); // Permite enviar datos en el cuerpo de la solicitud

	    // Escribe el JSON en el cuerpo de la solicitud
		try (OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		return getResponse(con);
	}

	// Ejecuta una solicitud GET y devuelve la respuesta como una cadena
	public String sendGet(String url, String token) throws Exception {
		URI uri = new URI(url);
	    URL endpointUrl = uri.toURL(); // Convierte URI a URL
	    HttpURLConnection con = (HttpURLConnection) endpointUrl.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + token); // Añade Bearer token para autenticación
		return getResponse(con);
	}

	// Recupera la respuesta generando un error si el estado no es 200 OK.
	private String getResponse(HttpURLConnection con) throws Exception {
		int responseCode = con.getResponseCode(); // Obtiene el estado de la conexión
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); // Lee la respuesta
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		if (responseCode != 200) {
			throw new RuntimeException("Failed: HTTP error code : " + responseCode);
		}

		return response.toString();
	}
}
