package Server.HttpHandlers;

import Server.controllers.UserController;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.sql.SQLException;
import org.json.JSONObject;


public class UserHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController =  null;
        userController = new UserController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathParts = path.split("/");
        try {
            switch (request) {
                case "GET":
                    if (pathParts.length == 3) {
                        if (pathParts[2].equals("all")) {
                            try {
                                response = userController.getAllUsers();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
                                response = userController.getUserByEmail(pathParts[2]);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                    break;
                case "POST":
                    if (pathParts.length == 2) {
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
                        try {
                            if (jsonObject.has("firstname") && jsonObject.has("lastname") && jsonObject.has("email") && jsonObject.has("password") && jsonObject.has("country") && jsonObject.has("city") && jsonObject.has("additionalname")){
                                userController.createUser(jsonObject.getString("firstname"), jsonObject.getString("lastname"), jsonObject.getString("email"), jsonObject.getString("password"), jsonObject.getString("country"), jsonObject.getString("city"), jsonObject.getString("additionalname"), null, null);
                                response = "user added successfully";
                            } else {
                                System.out.println("fail");
                                exchange.sendResponseHeaders(400, 0);
                                response = "Bad request";
                            }
                        } catch (Exception e) {
                            exchange.sendResponseHeaders(500, 0);
                            response = "Internal server error";
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            exchange.close();
        }
    }
}

