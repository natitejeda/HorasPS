package com.ps.App;

import org.json.JSONArray;
import java.io.FileWriter;
import java.io.IOException;

public class App {
	public static void main(String[] args) {
		HttpClient client = new HttpClient();
		String loginUrl = "http://kanban.psolutions.tech/users/login";
		String userCredentials = "{\"email\":\"natalia.tejeda@psolutions.tech\", \"password\":\"ps2024!\"}";

		try {
			// Hacer la solicitud POST para obtener el token
			String postResponse = client.sendPost(loginUrl, userCredentials);
			AuthResponse authResponse = AuthResponse.fromJson(postResponse);
			String token = authResponse.getToken();

			// Definir los IDs de los tableros
			String[] boardIds = { "cuTWNKH9KPCE6HzJW", "fZrASHjsNmSufRMQX", "JLCH7PQQ65Y2HYPHM", "37Yf72QHZQrsw9rLY" };

			// Crear una instancia de BoardService
			BoardService boardService = new BoardService();

			// Obtener los datos de los tableros
			JSONArray allBoardsData = boardService.getBoardData(boardIds, token);

			// Convertir el objeto JSON final a String y mostrarlo
			String allBoardsJsonString = allBoardsData.toString(4); // Con formato de 4 espacios
            // System.out.println(allBoardsJsonString);
			
			// Guardar JSON final en carpeta raíz
			try (FileWriter fileWriter = new FileWriter("file.json")) {
				fileWriter.write(allBoardsJsonString);
				System.out.println("Ready");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}