package Server.HttpHandlers;

import Server.controllers.LikeController;
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

public class LikeHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

            LikeController likeController = new LikeController();
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
                        response = handleGetRequest(exchange, likeController, pathParts);
                        break;
                    case "POST":
                        response = handlePostRequest(exchange, likeController, pathParts);
                        break;
                    case "DELETE":
                        response = handleDeleteRequest(exchange, likeController, pathParts);
                        break;
                    default:
                        response = "Method not allowed";
                        exchange.sendResponseHeaders(405, response.length());
                }
            } catch (Exception e) {
                response = "Internal server error";
                exchange.sendResponseHeaders(500, response.length());
            }
            sendResponse(exchange, response);
    }

    private String handleGetRequest(HttpExchange exchange, LikeController likeController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) {
            if (pathParts[2].equals("all")) {// get all user likes  /like/all
                response = likeController.getAllLikesOfOneUser(email);
                if (response == null) {
                    response = "No likes found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else { //like/post_id    get all likes of one post
                try {
                    int post_id = Integer.parseInt(pathParts[2]);
                    response = likeController.getAllLikesOfOnePost(post_id);
                    if (response == null) {
                        response = "No likes found";
                        exchange.sendResponseHeaders(404, response.length());
                    } else {
                        exchange.sendResponseHeaders(200, response.length());
                    }
                } catch (NumberFormatException e) {
                    response = "Invalid request";
                    exchange.sendResponseHeaders(400, response.length());
                }
            }
        } else if (pathParts.length == 4) { // like/all/all
            if (pathParts[2].equals("all") && pathParts[3].equals("all")) {
                response = likeController.getAllLikes();
                if (response == null) {
                    response = "No likes found";
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

    public String handlePostRequest(HttpExchange exchange, LikeController likeController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        PostController postController = new PostController();
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) { //like/post_id
            try {
            int post_id = Integer.parseInt(pathParts[2]);
            if (postController.postExists(post_id)) {
                if (likeController.likeExists(email, post_id)) {
                    response = "Like already exists";
                    exchange.sendResponseHeaders(400, response.length());
                } else {
                    likeController.addLike(email, post_id);
                    response = "Like added";
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else {
                response = "Post not found";
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

    public String handleDeleteRequest(HttpExchange exchange, LikeController likeController, String[] pathParts) throws IOException, SQLException {
        String response = "";
        PostController postController = new PostController();
        String email = JwtController.verifyToken(exchange);
        if (pathParts.length == 3) { //like/post_id
            try {
                int post_id = Integer.parseInt(pathParts[2]);
                if (postController.postExists(post_id)) {
                    if (likeController.likeExists(email, post_id)) {
                        likeController.deleteLike(email, post_id);
                        response = "Like deleted";
                        exchange.sendResponseHeaders(200, response.length());
                    } else {
                        response = "Like not found";
                        exchange.sendResponseHeaders(404, response.length());
                    }
                } else {
                    response = "Post not found";
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
/*
* <dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20210307</version> <!-- Use the latest version -->
</dependency>
* */
