package Server.HttpHandlers;

import Server.controllers.FollowController;
import Server.controllers.HashTagController;
import Server.utils.JwtController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class HashTagHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashTagController hashTagController=new HashTagController();
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
                    response = handleGEtRequest(exchange, hashTagController, pathParts);
                    break;
                default:
                    response = "Invalid request";
                    exchange.sendResponseHeaders(405, response.length());
            }
        }catch (SQLException|IOException e){
            response="Server side problem";
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
    public String handleGEtRequest(HttpExchange exchange,HashTagController hashTagController,String[] pathParts) throws SQLException, IOException {
        //hashTag/String tag
        String response="";
            if (pathParts.length == 2) {
                response = hashTagController.getByHashTag(pathParts[1]);
                if (response == null) {
                    response = "Not Found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                }
            } else {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
        return response;
    }
}
