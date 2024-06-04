package Server.HttpHandlers;

import Server.controllers.SearchController;
import Server.utils.JwtController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class SearchHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        SearchController searchController = new SearchController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        String response = "";

        try {
            try {
                String email = JwtController.verifyToken(exchange);
                if (email == null) {
                    response = "Unauthorized";
                    exchange.sendResponseHeaders(401, response.length());
                    sendResponse(exchange, response);
                    return;
                }
            } catch (Exception e) {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
                sendResponse(exchange, response);
                return;
            }
            if (request.equals("GET")) {
                response = handleGetRequest(exchange, searchController, pathParts);
            } else {
                response = "Invalid request";
                exchange.sendResponseHeaders(405, response.length());
            }
        } catch (Exception e) {
            response = "Internal server error";
            exchange.sendResponseHeaders(500, response.length());
            e.printStackTrace();
        }
        sendResponse(exchange, response);
    }

    public String handleGetRequest(HttpExchange exchange, SearchController searchController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        if (pathParts.length == 3) { //search/search_txt
            String search = pathParts[2];
            response = searchController.searchUser(search);
            if (response == null) {
                response = "not found";
                exchange.sendResponseHeaders(404, response.length());
            } else {
                exchange.sendResponseHeaders(200, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(405, response.length());
        }
        return response;
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
