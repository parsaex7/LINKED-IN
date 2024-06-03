package Server.HttpHandlers;

import Server.controllers.CommentController;
import Server.utils.JwtController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class CommentHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CommentController commentController = new CommentController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        String response = "";

        try {
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
            switch (request) {
                case "GET":
                    response = handleGetRequest(exchange, commentController, pathParts);
                    break;
                case "POST":
                    response = handlePostRequest(exchange, commentController, pathParts);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange, commentController, pathParts);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange, commentController, pathParts);
                    break;
                default:
                    response = "Method not allowed";
                    exchange.sendResponseHeaders(405, response.length());
            }
        } catch (Exception e) {
            response = "Internal server error";
            e.printStackTrace();
            exchange.sendResponseHeaders(500, response.length());
        } finally {
            sendResponse(exchange, response);
        }

    }

    private String handlePostRequest(HttpExchange exchange, CommentController commentController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        if (pathParts.length == 3) { //comment/post_id
            JSONObject jsonObject = getJsonObject(exchange);
            String user_email = JwtController.verifyToken(exchange);
            String comment = jsonObject.getString("comment");
            try {
                int post_id = Integer.parseInt(pathParts[2]);
                commentController.createComment(user_email, comment, post_id);
                response = "Comment created";
                exchange.sendResponseHeaders(200, response.length());
            } catch (NumberFormatException e) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handleGetRequest(HttpExchange exchange, CommentController commentController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        if (pathParts.length == 3) { // comment/comment_id
            try {
                int comment_id = Integer.parseInt(pathParts[2]);
                response = commentController.getComment(comment_id);
                if (response == null) {
                    response = "Comment not found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
            } catch (NumberFormatException e) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else if (pathParts.length == 4 && pathParts[2].equals("all")) { //comment/all/post_id
            try {
                int post_id = Integer.parseInt(pathParts[3]);
                response = commentController.getAllCommentsOfOnePost(post_id);
                if ( response == null) {
                    response = "No comments found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                    return response;
                }
            } catch (NumberFormatException e) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handleDeleteRequest(HttpExchange exchange, CommentController commentController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) { //comment/comment_id
            try {
                int comment_id = Integer.parseInt(pathParts[2]);
                if (commentController.isCommentExist(comment_id)) {
                    if (commentController.deleteComment(comment_id, email)) {
                        response = "Comment deleted";
                        exchange.sendResponseHeaders(200, response.length());
                    } else {
                        response = "Permission Denied OR Comment not found";
                        exchange.sendResponseHeaders(401, response.length());
                    }
                } else {
                    response = "Comment not found";
                    exchange.sendResponseHeaders(404, response.length());
                }
            } catch (NumberFormatException e) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handlePutRequest(HttpExchange exchange, CommentController commentController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) { //comment/comment_id
            JSONObject jsonObject = getJsonObject(exchange);
            String comment = jsonObject.getString("comment");
            try {
                int comment_id = Integer.parseInt(pathParts[2]);
                if (commentController.isCommentExist(comment_id, email)) {
                    if (commentController.updateComment(email, comment, comment_id)) {
                        response = "Comment updated";
                        exchange.sendResponseHeaders(200, response.length());
                    } else {
                        response = "Permission Denied OR Comment not found";
                        exchange.sendResponseHeaders(401, response.length());
                    }
                } else {
                    response = "Comment not found";
                    exchange.sendResponseHeaders(404, response.length());
                }
            } catch (NumberFormatException e) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
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

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
        exchange.sendResponseHeaders(200, response.length());
    }
}
