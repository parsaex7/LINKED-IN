package Server.HttpHandlers;

import Server.controllers.EducationController;
import Server.utils.JwtController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.SQLException;

public class EducationHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EducationController educationController = new EducationController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        String response = "";

        try {
            switch (request) {
                case "GET":
                    response = handleGetRequest(exchange, educationController, pathParts);
                    break;
                case "POST":
                    response = handlePostRequest(exchange, educationController, pathParts);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange, educationController, pathParts);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange, educationController, pathParts);
                    break;
                default:
                    response = "Method not allowed";
                    exchange.sendResponseHeaders(405, response.length());
                    sendResponse(exchange, response);
            }
            sendResponse(exchange, response);
        } catch (Exception e) {
            response = "Internal server error";
            exchange.sendResponseHeaders(500, response.length());
            sendResponse(exchange, response);
            e.printStackTrace();
        }
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
        return jsonObject.has("school") && jsonObject.has("degree") && jsonObject.has("fieldofstudy") && jsonObject.has("grade") && jsonObject.has("detail") && jsonObject.has("startdate") && jsonObject.has("enddate") && jsonObject.has("workdetail") && jsonObject.has("accessedu");
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
        exchange.sendResponseHeaders(200, response.length());
    }

    private String handleGetRequest(HttpExchange exchange, EducationController educationController, String[] pathParts) throws SQLException, IOException {
        String response = "";
        if (pathParts.length == 3) {
            if (pathParts[2].equals("all")) { // education/all
                response = educationController.getAllEducations();
                exchange.sendResponseHeaders(200, response.length());
            } else { // education/schoolName
                String school = pathParts[2];
                String email = JwtController.verifyToken(exchange);
                if (email != null) {
                    response = educationController.getEducationByEmailAndSchool(email, school);
                    if (response == null) {
                        response = "Education not found";
                        exchange.sendResponseHeaders(404, response.length());
                    } else {
                        exchange.sendResponseHeaders(200, response.length());
                    }
                } else {
                    response = "Unauthorized";
                    exchange.sendResponseHeaders(401, response.length());
                }
            }
        } else if (pathParts.length == 2) { // /education -> return all of that person's educations
            String email = JwtController.verifyToken(exchange);
            if (email != null) {
                response = educationController.getEducations(email);
                if (response == null) {
                    response = "Empty!";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
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

    private String handlePostRequest(HttpExchange exchange, EducationController educationController, String[] pathParts) throws SQLException, IOException {
        String response = "";
        if (pathParts.length == 2) {
            String email = JwtController.verifyToken(exchange);
            JSONObject jsonObject = getJsonObject(exchange);
            if (!isValidJson(jsonObject)) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
                sendResponse(exchange, response);
                return response;
            }
            if (email != null) {
                if (educationController.getEducationByEmailAndSchool(email, jsonObject.getString("school")) == null) {
                    educationController.createEducation(
                            jsonObject.getString("school"),
                            jsonObject.getString("degree"),
                            jsonObject.getString("fieldofstudy"),
                            jsonObject.getDouble("grade"),
                            jsonObject.getString("detail"),
                            new Date(jsonObject.getLong("startdate")),
                            new Date(jsonObject.getLong("enddate")),
                            jsonObject.getString("workdetail"),
                            jsonObject.getString("accessedu"),
                            email);
                    response = "Education added successfully";
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "Education already exists";
                    exchange.sendResponseHeaders(409, response.length());
                }
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

    private String handleDeleteRequest(HttpExchange exchange, EducationController educationController, String[] pathParts) throws SQLException, IOException {
        String response = "";
        if (pathParts.length == 2) {
            String email = JwtController.verifyToken(exchange);
            if (email == null) {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            } else if (educationController.getEducations(email) != null) {
                educationController.deleteEducationByEmail(email);
                response = "Education deleted successfully";
                exchange.sendResponseHeaders(200, response.length());
            } else {
                response = "Education not found";
                exchange.sendResponseHeaders(404, response.length());
            }
        } else if (pathParts.length == 3) {
            String email = JwtController.verifyToken(exchange);
            String school = pathParts[2];
            if (email == null) {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            } else if (educationController.getEducationByEmailAndSchool(email, school) != null) {
                educationController.deleteEducationByEmailAndSchool(email, school);
                response = "Education deleted successfully";
                exchange.sendResponseHeaders(200, response.length());
            } else {
                response = "Education not found";
                exchange.sendResponseHeaders(404, response.length());
            }
        } // education/schoolName
        else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handlePutRequest(HttpExchange exchange, EducationController educationController, String[] pathParts) throws SQLException, IOException {
        String response = "";
        if (pathParts.length == 2) { // education -> update education
            String email = JwtController.verifyToken(exchange);
            JSONObject jsonObject = getJsonObject(exchange);
            String school = jsonObject.getString("school");
            if (!isValidJson(jsonObject)) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
                sendResponse(exchange, response);
                return response;
            }
            if (email == null) {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            } else {
                if (educationController.getEducationByEmailAndSchool(email, school) != null) {
                    educationController.updateEducation(
                            jsonObject.getString("school"),
                            jsonObject.getString("degree"),
                            jsonObject.getString("fieldofstudy"),
                            jsonObject.getDouble("grade"),
                            jsonObject.getString("detail"),
                            new Date(jsonObject.getLong("startdate")),
                            new Date(jsonObject.getLong("enddate")),
                            jsonObject.getString("workdetail"),
                            jsonObject.getString("accessedu"),
                            email);
                    response = "Education updated successfully";
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "Education not found";
                    exchange.sendResponseHeaders(404, response.length());
                }
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }
}
