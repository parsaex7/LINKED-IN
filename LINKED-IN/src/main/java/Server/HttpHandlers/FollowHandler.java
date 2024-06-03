package Server.HttpHandlers;

import Server.Exceptions.AccessDeniedException;
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
                case "POST":
                    response = handlePostRequest(exchange, followController, pathParts);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange, followController, pathParts);
                    break;
                case "GET":
                    response = handleGetRequest(exchange, followController, pathParts);
                    break;
                default:
                    response = "Invalid request";
                    exchange.sendResponseHeaders(405, response.length());
            }
        } catch (Exception e) {
            response = "Internal server error";
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

    private String handlePostRequest(HttpExchange exchange, FollowController followController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) {//follow/follower_email
            String follower_email = pathParts[2];
            try {
                if (followController.isFollowExist(follower_email, email)) {
                    response = "User already followed";
                    exchange.sendResponseHeaders(400, response.length());
                    return response;
                }
                followController.createFollow(follower_email, email);
                response = "User followed successfully";
                exchange.sendResponseHeaders(200, response.length());
            } catch (UserNotExistException e) {
                response = "User not found";
                exchange.sendResponseHeaders(404, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    public String handleDeleteRequest(HttpExchange exchange, FollowController followController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 4) {//follow/follower_email/following_email
            try {
                String follower_email = pathParts[2];
                String following_email = pathParts[3];
                if (!followController.isFollowExist(follower_email, following_email)) {
                    response = "User not followed";
                    exchange.sendResponseHeaders(400, response.length());
                    return response;
                }
                followController.deleteFollow(follower_email, following_email, email);
                response = "User unfollowed successfully OR User deleted from followers";
                exchange.sendResponseHeaders(200, response.length());
            } catch (UserNotExistException e) {
                response = "user not found";
                exchange.sendResponseHeaders(404, response.length());
            } catch (AccessDeniedException e) {
                response = "Access denied";
                exchange.sendResponseHeaders(403, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    public String handleGetRequest(HttpExchange exchange, FollowController followController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) {//follow/followers
            if (pathParts[2].equals("follower")) {
                response = followController.getAllFollowers(email);
                if (response == null) {
                    response = "No followers found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else if (pathParts[2].equals("following")) {
                response = followController.getAllFollowings(email);
                if (response == null) {
                    response = "No followings found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }

        } else if (pathParts.length == 4) {//follow/followings/email
            if (pathParts[2].equals("following")) {
                String user_email = pathParts[3];
                response = followController.getAllFollowings(user_email);
                if (response == null) {
                    response = "No followings found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else if (pathParts[2].equals("follower")) {
                String user_email = pathParts[3];
                response = followController.getAllFollowers(user_email);
                if (response == null) {
                    response = "No followers found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }
}
