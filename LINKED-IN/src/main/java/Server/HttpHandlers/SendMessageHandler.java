package Server.HttpHandlers;

import Server.controllers.PrivateMessageController;
import Server.utils.JwtController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SendMessageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        PrivateMessageController privateMessageController = new PrivateMessageController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        String response = "";

        try {
            String email = JwtController.verifyToken(exchange);
            if (email == null) {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
                sendResponse(exchange, response);
            }
        } catch (Exception e) {
            response = "Unauthorized";
            exchange.sendResponseHeaders(401, response.length());
            sendResponse(exchange, response);
        }
        try {
            switch (request) {
                case "POST":
                    response = handlePostRequest(exchange, privateMessageController, pathParts);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange, privateMessageController, pathParts);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange, privateMessageController, pathParts);
                    break;
                default:
                    response = "Invalid Request";
                    exchange.sendResponseHeaders(405, response.length());
            }
        } catch (Exception e) {
            response = "Internal server error";
            exchange.sendResponseHeaders(500, response.length());
            e.printStackTrace();
        }
        sendResponse(exchange, response);
    }

    private String handlePutRequest(HttpExchange exchange, PrivateMessageController privateMessageController, String[] pathParts) {
        return null;
    }

    private String handleDeleteRequest(HttpExchange exchange, PrivateMessageController privateMessageController, String[] pathParts) {
        return null;
    }

    private String handlePostRequest(HttpExchange exchange, PrivateMessageController privateMessageController, String[] pathParts) {
        return null;
    }


    private static JSONObject getJsonObject(HttpExchange exchange) throws IOException {
        try (InputStream requestBody = exchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody))) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            return new JSONObject(body.toString());
        }
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
        exchange.sendResponseHeaders(200, response.length());
    }
}
