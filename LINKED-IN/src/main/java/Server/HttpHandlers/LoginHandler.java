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
                            response = "inputError";
                            exchange.sendResponseHeaders(404, response.length());
                        } else {
                            String token = JwtController.createToken(email);
                            Headers responseHeaders = exchange.getResponseHeaders();
                            responseHeaders.add("JWT", token); // Add JWT to response headers
                            response = userController.getUserByEmail(email);
                            exchange.sendResponseHeaders(200, response.length());
                        }
                    } catch (Exception e) {
                        response = "serverError";
                        exchange.sendResponseHeaders(500, response.length());
                        e.printStackTrace();
                    } finally {
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } else {
                    response = "invalidRequest";
                    exchange.sendResponseHeaders(400, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
                break;

            default:
                response = "invalidRequest";
                exchange.sendResponseHeaders(405, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                break;
        }
    }
}
