package Server.HttpHandlers;

import Server.controllers.UserController;
import Server.utils.JwtController;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = new UserController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] pathParts = path.split("/");

        switch(request) {
            case "GET":
                if (pathParts.length == 4) { // login/email/password
                    String email = pathParts[2];
                    String password = pathParts[3];
                    try {
                        response = userController.getUserByEmailAndPassword(email, password);
                        if (response == null) {
                            response = "User not found OR invalid password";
                            exchange.sendResponseHeaders(404, response.length());
                        } else {
                            String token = JwtController.createToken(email);
                            System.out.println(token);
                            Headers responseHeaders = exchange.getResponseHeaders();
                            responseHeaders.add("JWT", token); // Add JWT to response headers
                            response = "Welcome " + email + ", your token is: " + token;
                            System.out.println(response);
                            exchange.sendResponseHeaders(200, response.length());
                        }
                    } catch (Exception e) {
                        response = "Error in login user";
                        exchange.sendResponseHeaders(500, response.length());
                        System.out.println("Error in login user");
                        e.printStackTrace();
                    } finally {
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } else {
                    response = "Invalid request";
                    exchange.sendResponseHeaders(400, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
                break;

            default:
                response = "Method not allowed";
                exchange.sendResponseHeaders(405, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                break;
        }
    }
}
