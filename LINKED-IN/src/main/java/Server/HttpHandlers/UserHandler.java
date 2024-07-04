package Server.HttpHandlers;

import Server.Exceptions.DuplicateUserException;
import Server.Exceptions.UserNotExistException;
import Server.controllers.UserController;
import Server.utils.JwtController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.Date;
import java.sql.SQLException;

public class UserHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        UserController userController = new UserController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        String response = "";

        try {
            switch (request) {
                case "GET":
                    response = handleGetRequest(exchange, userController, pathParts);
                    break;
                case "POST":
                    response = handlePostRequest(exchange, userController, pathParts);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange, userController, pathParts);
                    break;
                default:
                    response = "Method not allowed";
                    exchange.sendResponseHeaders(405, response.length());
            }
        } catch (Exception e) {
            response = "serverError";
            exchange.sendResponseHeaders(500, response.length());
            e.printStackTrace();
        } finally {
            sendResponse(exchange, response);
        }
    }

    private String handleGetRequest(HttpExchange exchange, UserController userController, String[] pathParts) throws SQLException, IOException {
        String response = "";
        if (pathParts.length == 3) {
            if (pathParts[2].equals("all")) { // user/all
                response = userController.getAllUsers();
                exchange.sendResponseHeaders(200, response.length());
            } else { // user/email
                try {
                    System.out.println(pathParts[2]);
                    response = userController.getUserByEmail(pathParts[2]);
                    exchange.sendResponseHeaders(200, response.length());
                } catch (UserNotExistException e) {
                    response = "User not found";
                    exchange.sendResponseHeaders(404, response.length());
                }
            }
        } else {
            response = "Invalid path";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handlePostRequest(HttpExchange exchange, UserController userController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        if (pathParts.length == 2) {
            try {
                JSONObject jsonObject = getJsonObject(exchange);
                if (isValidJson(jsonObject)) {
                    userController.signupUser(
                            jsonObject.getString("firstname"),
                            jsonObject.getString("lastname"),
                            jsonObject.getString("email"),
                            jsonObject.getString("password"));
                    response = JwtController.createToken(jsonObject.getString("email"));
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "invalidRequest";
                    exchange.sendResponseHeaders(400, response.length());
                }
            } catch (DuplicateUserException e) {
                response = "duplicate";
                exchange.sendResponseHeaders(409, response.length());
            }
        } else {
            response = "invalidRequest";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handleDeleteRequest(HttpExchange exchange, UserController userController, String[] pathParts) throws SQLException, UserNotExistException, IOException {
        String response = "";
        if (pathParts.length == 3) {
            userController.deleteUserByEmail(pathParts[2]);
            response = "User deleted successfully";
            exchange.sendResponseHeaders(200, response.length());
        } else {
            response = "Invalid path";
            exchange.sendResponseHeaders(400, response.length());
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

    private static boolean isValidJson(JSONObject jsonObject) {
        return jsonObject.has("firstname") && jsonObject.has("lastname") &&
                jsonObject.has("email") && jsonObject.has("password");
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        exchange.close();
    }
}
