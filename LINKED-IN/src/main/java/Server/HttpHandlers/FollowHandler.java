package Server.HttpHandlers;

import Server.Exceptions.AlreadyFollowedException;
import Server.Exceptions.UserNotExistException;
import Server.Exceptions.followingNotFound;
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
                followController.follow(email, follower_email);  //email wants to follow follower_email
                response = "User followed successfully";
                exchange.sendResponseHeaders(200, response.length());
            } catch (UserNotExistException e) {
                response = "User not found";
                exchange.sendResponseHeaders(404, response.length());
            } catch (AlreadyFollowedException e) {
                response = "User already followed";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

        private String handleDeleteRequest(HttpExchange exchange, FollowController followController, String[] pathParts) throws IOException, SQLException{
            String response = "";
            String email = JwtController.verifyToken(exchange);
            if (pathParts.length == 3) {//follow/follower_email
                try {
                    String follower_email = pathParts[2];
                    followController.unFollow(email, follower_email);  //email wants to unfollow follower_email
                    response = "User unfollowed successfully";
                    exchange.sendResponseHeaders(200, response.length());
                } catch (UserNotExistException e) {
                    response = "user not found";
                    exchange.sendResponseHeaders(404, response.length());
                } catch (followingNotFound e) {
                    response = "User not found in followers";
                    exchange.sendResponseHeaders(404, response.length());
                }
            } else {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
            return response;
        }


        public String handleGetRequest (HttpExchange exchange, FollowController followController, String[]
        pathParts) throws IOException, SQLException {
            String response = "";
            String email = JwtController.verifyToken(exchange);
            if (pathParts.length == 3) {
                if (pathParts[2].equals("follower")) { //follow/follower  -> get all followers of one user
                    try {
                        response = followController.getFollowers(email);
                        exchange.sendResponseHeaders(200, response.length());
                    } catch (followingNotFound e) {
                        response = "No followers found";
                        exchange.sendResponseHeaders(410, response.length());
                    } catch (UserNotExistException e) {
                        response = "User not found";
                        exchange.sendResponseHeaders(404, response.length());
                    }
                } else if (pathParts[2].equals("following")) { //follow/following  -> get all followings of one user
                    try {
                        response = followController.getFollowings(email);
                        exchange.sendResponseHeaders(200, response.length());
                    } catch (followingNotFound e) {
                        response = "No followings found";
                        exchange.sendResponseHeaders(410, response.length());
                    } catch (UserNotExistException e) {
                        response = "User not found";
                        exchange.sendResponseHeaders(404, response.length());
                    }
                } else {
                    response = "Invalid request";
                    exchange.sendResponseHeaders(400, response.length());
                }

            } else if (pathParts.length == 4) {//follow/followings/email  --> get all followings of one user
                if (pathParts[2].equals("following")) {
                    String user_email = pathParts[3];
                    try {
                        response = followController.getFollowings(user_email);
                        exchange.sendResponseHeaders(200, response.length());
                    } catch (followingNotFound e) {
                        response = "No followings found";
                        exchange.sendResponseHeaders(404, response.length());
                    } catch (UserNotExistException e) {
                        response = "User not found";
                        exchange.sendResponseHeaders(404, response.length());
                    }
                } else if (pathParts[2].equals("follower")) {  //follow/followers/email  --> get all followers of one user
                    String user_email = pathParts[3];
                    try {
                        response = followController.getFollowers(user_email);
                        exchange.sendResponseHeaders(200, response.length());
                    } catch (followingNotFound e) {
                        response = "No followers found";
                        exchange.sendResponseHeaders(404, response.length());
                    } catch (UserNotExistException e) {
                        response = "User not found";
                        exchange.sendResponseHeaders(404, response.length());
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
