package Server.HttpHandlers;

import Server.controllers.ContactController;
import Server.controllers.PostController;
import Server.utils.JwtController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class PostHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        PostController postController = new PostController();
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
                    response = handleGetRequest(exchange, postController, pathParts);
                    break;
                case "POST":
                    response = handlePostRequest(exchange, postController, pathParts);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange, postController, pathParts);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange, postController, pathParts);
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

    private String handleGetRequest(HttpExchange exchange, PostController postController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (email == null) {
            response = "Unauthorized";
            exchange.sendResponseHeaders(401, response.length());
        }
        if (pathParts.length == 3) {
            if (pathParts[2].equals("all")) { // post/all
                response = postController.getAllPostsOfOneUser(email);
                if (response == null) {
                    response = "No posts found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else { //post/post_id
                try {
                    int postId = Integer.parseInt(pathParts[2]);
                    response = postController.getPost(postId, email);
                    if (response != null) {
                        exchange.sendResponseHeaders(200, response.length());
                    } else {
                        response = "Post not found OR access denied";
                        exchange.sendResponseHeaders(404, response.length());
                    }
                } catch (NumberFormatException e) {
                    response = "Invalid post id";
                    exchange.sendResponseHeaders(400, response.length());
                }
            }
        } else if (pathParts.length == 4) {
            if (pathParts[2].equals("all") && pathParts[3].equals("all")) { //post/all/all
                response = postController.getAllPosts();
                if (response != null) {
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "No posts found";
                    exchange.sendResponseHeaders(404, response.length());
                }
            }
        } else {
            response = "Invalid path";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handlePostRequest(HttpExchange exchange, PostController postController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        if (pathParts.length == 2) {
            JSONObject jsonObject = getJsonObject(exchange);
            String message = jsonObject.getString("message");
            String email = JwtController.verifyToken(exchange);
            if (email != null) {
                postController.addPost(message, email);
                response = "Post added";
                exchange.sendResponseHeaders(200, response.length());
            } else {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            }
        } else {
            response = "Invalid path";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handleDeleteRequest(HttpExchange exchange, PostController postController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) { //post/all or post/post_id
            if (pathParts[2].equals("all")) {
                postController.deleteAllPostsOfOneUser(email);
                response = "All posts deleted";
                exchange.sendResponseHeaders(200, response.length());
            } else {
                try {
                    int postId = Integer.parseInt(pathParts[2]);
                    if (postController.getPost(postId, email) == null) {
                        response = "Not found OR Permission denied";
                        exchange.sendResponseHeaders(404, response.length());
                    } else {
                        postController.deletePost(postId, email);
                        response = "Post deleted";
                        exchange.sendResponseHeaders(200, response.length());
                    }
                } catch (NumberFormatException e) {
                    response = "Invalid post id";
                    exchange.sendResponseHeaders(400, response.length());
                }
            }
        } else if (pathParts.length == 4) { //post/all/all
            if (pathParts[2].equals("all") && pathParts[3].equals("all")) {
                postController.deleteAllPosts();
                response = "All posts deleted";
                exchange.sendResponseHeaders(200, response.length());
            } else {
                response = "Invalid path";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid path";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }

    private String handlePutRequest(HttpExchange exchange, PostController postController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) { //post/post_id
            JSONObject jsonObject = getJsonObject(exchange);
            String message = jsonObject.getString("message");
            try {
                int postId = Integer.parseInt(pathParts[2]);
                if (postController.getPost(postId, email) != null) {
                    postController.updatePost(postId, message, email);
                    response = "Post updated";
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "Post not found OR Permission denied";
                    exchange.sendResponseHeaders(404, response.length());
                }
            } catch (NumberFormatException e) {
                response = "Invalid post id";
                exchange.sendResponseHeaders(400, response.length());
            }
        } else {
            response = "Invalid path";
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
