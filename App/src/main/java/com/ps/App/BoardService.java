package com.ps.App;

import org.json.JSONArray;
import org.json.JSONObject;

public class BoardService {

	private final HttpClient client;

	// Constructor que inicializa el HttpClient
	public BoardService() {
		this.client = new HttpClient();
	}

	// Método para obtener los datos de varios tableros
	public JSONArray getBoardData(String[] boardIds, String token) {
		JSONArray allBoardsData = new JSONArray();

		for (String boardId : boardIds) {
			try {
				// Construir la URL del tablero
				String getUrl = "http://kanban.psolutions.tech/api/boards/" + boardId + "/export";
				String getResponse = client.sendGet(getUrl, token);

				// Convertir la respuesta a un objeto JSON
				JSONObject jsonGetResponse = new JSONObject(getResponse);

				// Crear un nuevo objeto JSON que almacenará la estructura modificada
				JSONObject jsonData = new JSONObject();

				// Obtener la lista de cards (tarjetas) del objeto original
				JSONArray responseCards = jsonGetResponse.getJSONArray("cards");

				// Crear un nuevo arreglo JSON para las tarjetas modificadas
				JSONArray cards = new JSONArray();

				// Iterar sobre las tarjetas originales y agregar los campos adicionales
				for (int i = 0; i < responseCards.length(); i++) {
					JSONObject responseCard = responseCards.getJSONObject(i);

					// Verificar si la tarjeta está archivada
					boolean archivedCard = responseCard.getBoolean("archived");

					if (!archivedCard) {
						// Crear un nuevo objeto JSON para la tarjeta modificada
						JSONObject card = new JSONObject();

						// Extraer el título
						String cardTitle = responseCard.getString("title");
						String[] cardTitleParts = cardTitle.split("-", 3); // Limitar el split a 3 partes

						// Asignar valores por defecto
						String clientID = "";
						String ticketID = "";
						String ticketDescription = "";

						// Verificar que el título tiene al menos dos partes (clientID y ticketID/descriptionID)
						if (cardTitleParts.length >= 2) {
							clientID = cardTitleParts[0].trim(); // Primer parte es clientID
							
							// Elimina espacios en blanco
						    String potentialTicketID = cardTitleParts[1].trim(); 

						    // Verifica si la segunda parte es numérica después de eliminar cualquier espacio
						    if (potentialTicketID.matches("\\d+")) { // Si es numérico, es el ticketID
						        ticketID = potentialTicketID;
						        ticketDescription = cardTitleParts.length == 3 ? cardTitleParts[2].trim() : ""; // Tercer parte es la descripción
						    } else {
						        // Si la segunda parte no es numérica, la consideramos como parte de la descripción
						        ticketDescription = potentialTicketID;
						    }
						}

						// Agregar los campos específicos al objeto de tarjeta nuevo
						card.put("title", cardTitle);
						card.put("clientID", clientID);
						card.put("ticketID", ticketID);
						card.put("ticketDescription", ticketDescription);

						// Agregar la tarjeta modificada al nuevo arreglo de tarjetas
						cards.put(card);
					};
				}

				// Copiar el "title" del tablero original
				jsonData.put("title", jsonGetResponse.getString("title"));
				// Copiar el "_id" del tablero original
				jsonData.put("_id", jsonGetResponse.getString("_id"));
				// Agregar el nuevo arreglo de tarjetas al objeto JSON modificado
				jsonData.put("cards", cards);

				// Agregar el objeto de tablero modificado al arreglo de tableros
				allBoardsData.put(jsonData);

			} catch (Exception e) {
				e.printStackTrace(); // Manejo de errores
			}
		}

		return allBoardsData; // Retornar todos los datos de los tableros
	}
}
