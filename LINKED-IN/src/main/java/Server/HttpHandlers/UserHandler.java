package Server.HttpHandlers;

import Server.Exceptions.DuplicateUserException;
import Server.Exceptions.UserNotExistException;
import Server.controllers.UserController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.sql.SQLException;

import org.json.JSONObject;


public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = new UserController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathParts = path.split("/");
        switch (request) {
            case "GET":
                if (pathParts.length == 3) {
                    if (pathParts[2].equals("all")) {
                        try {
                            response = userController.getAllUsers();
                        } catch (SQLException e) {
                            System.out.println("Error in getting all users(SQL)");
                            e.printStackTrace();
                        } catch (JsonProcessingException e) {
                            System.out.println("Error in getting all users(Json)");
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            response = userController.getUserByEmail(pathParts[2]);
                        } catch (SQLException e) {
                            System.out.println("Error in getting user by email(SQL)");
                            e.printStackTrace();
                        } catch (JsonProcessingException e1) {
                            System.out.println("Error in getting user by email(Json)");
                            e1.printStackTrace();
                        } catch (UserNotExistException e2) {
                            System.out.println(e2);
                        }
                    }
                }
                break;
            case "POST":
                boolean status = false;
                if (pathParts.length == 2) {
                    try {
                        try {
                            JSONObject jsonObject = getJsonObject(exchange);
                            if (jsonObject.has("firstname") && jsonObject.has("lastname") && jsonObject.has("email") && jsonObject.has("password") && jsonObject.has("country") && jsonObject.has("city") && jsonObject.has("additionalname")) {
                                userController.createUser(jsonObject.getString("firstname"), jsonObject.getString("lastname"), jsonObject.getString("email"), jsonObject.getString("password"), jsonObject.getString("country"), jsonObject.getString("city"), jsonObject.getString("additionalname"), null, null);
                                response = "user added successfully";
                                status = true;
                            } else {
                                exchange.sendResponseHeaders(400, 0);
                                response = "Bad request";
                            }
                        } catch (IOException e) {
                            exchange.sendResponseHeaders(400, 0);
                            System.out.println("Error in getting json object(read http header and body) OR bad request");
                            response = "Bad request";
                            e.printStackTrace();
                        } catch (SQLException e) {
                            exchange.sendResponseHeaders(500, 0);
                            response = "Internal server error";
                            System.out.println("Error in creating user(SQL)");
                            e.printStackTrace();
                        } catch (DuplicateUserException e) {
                            exchange.sendResponseHeaders(409, 0);
                            response = "User already exists";
                            System.out.println(e);
                        }
                    } catch (Exception e) {
                        exchange.sendResponseHeaders(500, 0);
                        response = "Internal server error";
                        e.printStackTrace();
                    }
                }
                if (status) {
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                }
                break;
            case "DELETE":
                if (pathParts.length == 3) {
                    try {
                        userController.deleteUserByEmail(pathParts[2]);
                        response = "user deleted successfully";
                        exchange.sendResponseHeaders(200, response.getBytes().length); // Add this line
                    } catch (SQLException e) {
                        exchange.sendResponseHeaders(500, 0);
                        response = "Internal server error";
                        System.out.println("Error in deleting user(SQL)");
                        e.printStackTrace();
                    } catch (UserNotExistException e) {
                        exchange.sendResponseHeaders(404, 0);
                        response = "User not found";
                        System.out.println(e);
                    }
                }
                break;
        }
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        exchange.close();
    }

    private static JSONObject getJsonObject(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        requestBody.close();

        String requestBodyString = body.toString();
        JSONObject jsonObject = new JSONObject(requestBodyString);
        return jsonObject;
    }
}


