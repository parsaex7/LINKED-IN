package Server.HttpHandlers;

import Server.controllers.ContactController;
import Server.utils.JwtController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ContactHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        ContactController ContactController = new ContactController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        String response = "";

        try {
            switch (request) {
                case "GET":
                    response = handleGetRequest(exchange, ContactController, pathParts);
                    break;
                case "POST":
                    response = handlePostRequest(exchange, ContactController, pathParts);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange, ContactController, pathParts);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange, ContactController, pathParts);
                    break;
                default:
                    response = "Method not allowed";
                    exchange.sendResponseHeaders(405, response.length());
            }
        } catch (Exception e) {
            response = "Internal server error";
            exchange.sendResponseHeaders(500, response.length());
            e.printStackTrace();
        }
        sendResponse(exchange, response);

    }

    private String handleGetRequest(HttpExchange exchange, ContactController contactController, String[] pathParts) throws SQLException, IOException {
        String response = "";
        if (pathParts.length == 3) {
            if (pathParts[2].equals("all")) { // contact/all
                response = contactController.getContacts();
                exchange.sendResponseHeaders(200, response.length());
            } else {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else if (pathParts.length == 2) {
            String email = JwtController.verifyToken(exchange);
            if (email != null) {
                response = contactController.getContact(email);
                if (response == null) {
                    response = "Contact not found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                    return response;
                }
            } else {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            }
        }
        else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }

        return response;
    }

    private String handlePostRequest(HttpExchange exchange, ContactController contactController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        if (pathParts.length == 2) {

            String email = JwtController.verifyToken(exchange);
            JSONObject jsonObject = getJsonObject(exchange);

            if (!isValidJson(jsonObject)) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
                return response;
            }

            if (email != null) {
                if (contactController.getContact(email) != null) {
                    contactController.updateContact(
                            jsonObject.getString("profilelink"),
                            email,
                            jsonObject.getString("phonenumber"),
                            jsonObject.getString("numbertype"),
                            jsonObject.getString("address"),
                            jsonObject.getString("contactid"),
                            jsonObject.getString("birthdayaccess")
                    );
                    response = "Contact updated successfully";
                } else {
                    contactController.createContact(
                            jsonObject.getString("profilelink"),
                            email,
                            jsonObject.getString("phonenumber"),
                            jsonObject.getString("numbertype"),
                            jsonObject.getString("address"),
                            jsonObject.getString("contactid"),
                            jsonObject.getString("birthdayaccess")
                    );
                    response = "Contact added successfully";
                }
                exchange.sendResponseHeaders(200, response.length());
            } else {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            }

        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handleDeleteRequest(HttpExchange exchange, ContactController contactController, String[] pathParts) throws SQLException, IOException {
        String response = "";
        if (pathParts.length == 2) {

            String email = JwtController.verifyToken(exchange);

            if (email == null) {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            } else {
                contactController.deleteContact(email);
                response = "Contact deleted successfully";
                exchange.sendResponseHeaders(200, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handlePutRequest(HttpExchange exchange, ContactController contactController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        if (pathParts.length == 2) {

            String email = JwtController.verifyToken(exchange);
            JSONObject jsonObject = getJsonObject(exchange);

            if (!isValidJson(jsonObject)) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
                return response;
            }

            if (email == null) {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            } else {
                if (contactController.getContact(email) != null) {
                    contactController.updateContact(
                            jsonObject.getString("profilelink"),
                            email,
                            jsonObject.getString("phonenumber"),
                            jsonObject.getString("numbertype"),
                            jsonObject.getString("address"),
                            jsonObject.getString("contactid"),
                            jsonObject.getString("birthdayaccess")
                    );
                    response = "Contact updated successfully";
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "Contact not found";
                    exchange.sendResponseHeaders(404, response.length());
                }
            }
        } else {
            response = "Invalid request";
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

    private boolean isValidJson(JSONObject jsonObject) {
        return jsonObject.has("profilelink") && jsonObject.has("phonenumber") && jsonObject.has("numbertype") && jsonObject.has("address") && jsonObject.has("contactid") && jsonObject.has("birthdayaccess");
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
        exchange.sendResponseHeaders(200, response.length());
    }
}
