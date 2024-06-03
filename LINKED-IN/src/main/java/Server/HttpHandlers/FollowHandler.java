package Server.HttpHandlers;

import Server.Exceptions.UserNotExistException;
import Server.controllers.FollowController;
import Server.utils.JwtController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class FollowHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        FollowController followController = new FollowController();
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
                return;
            }
        } catch (Exception e) {
            response = "Unauthorized";
            exchange.sendResponseHeaders(401, response.length());
            sendResponse(exchange, response);
            return;
        }
        try {
            switch (request) {
                case "GET":
                    response = handleGetRequest(exchange, followController, pathParts);
                    break;
                case "POST":
                    response = handlePostRequest(exchange, followController, pathParts);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange, followController, pathParts);
                    break;
                default:
                    response = "Method not allowed";
                    exchange.sendResponseHeaders(405, response.length());
            }
        } catch (Exception e) {
            response = "Server side problem";
            exchange.sendResponseHeaders(500, response.length());
        }
        sendResponse(exchange, response);

    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        exchange.close();
    }

    private String handlePostRequest(HttpExchange exchange, FollowController followController, String[] pathParts) throws IOException {
        String response = "";
        String email1 = JwtController.verifyToken(exchange);
        if (pathParts.length == 2) {//follow/email2
            try {
                followController.follow(email1, pathParts[1]);
                response = "followed";
                exchange.sendResponseHeaders(200, response.length());
            } catch (UserNotExistException e) {
                response = "user not found";
                exchange.sendResponseHeaders(404, response.length());
            } catch (IOException | SQLException e) {
                response = "Server side problem";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    public String handleDeleteRequest(HttpExchange exchange, FollowController followController, String[] pathParts) throws IOException {
        String response ="";
        String email1 = JwtController.verifyToken(exchange);
        if (pathParts.length == 2) {//unFollow/email2
            try {
                followController.unFollow(email1, pathParts[1]);
                response = "Unfollowed";
                exchange.sendResponseHeaders(200, response.length());
            } catch (UserNotExistException e) {
                response = "user not found";
                exchange.sendResponseHeaders(404, response.length());
            } catch (IOException | SQLException e) {
                response = "Server side problem";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    public String handleGetRequest(HttpExchange exchange, FollowController followController, String[] pathParts) throws IOException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if(email==null){
            response="user not found";
            exchange.sendResponseHeaders(400, response.length());
        }
        else if (pathParts.length == 2) {
            try {
                if (pathParts[1].equals("getfollowers")) {//follow/getfollowers
                    response = followController.getFollowers(email);
                    exchange.sendResponseHeaders(200, response.length());
                } else if (pathParts[1].equals("getfollowings")) {//follow/getfollowings
                    response = followController.getFollowings(email);
                    exchange.sendResponseHeaders(200, response.length());
                }
            } catch (Exception e) {
                response = "Server side problem";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }
}
