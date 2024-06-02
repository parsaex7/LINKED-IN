package Server.HttpHandlers;

import Server.controllers.EducationController;
import Server.utils.JwtController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class EducationHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EducationController educationController=new EducationController();
        String request = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        String response ="";
        try {
            switch (request){
                case "GET" :
                    response=handleGetRequest(exchange,educationController,pathParts);
                    break;
                case "POST":
                    response=handlePostRequest(exchange,educationController,pathParts);
                case "DELETE":
                    response=handleDeleteRequest(exchange,educationController,pathParts);
                    break;
                case "PUT":
                    response=handlePutRequest(exchange,educationController,pathParts);
                    break;
                default:
                    response = "Method not allowed";
                    exchange.sendResponseHeaders(405, response.length());
            }
        }catch (Exception e){
            response = "Internal server error";
            exchange.sendResponseHeaders(500, response.length());
            e.printStackTrace();
        }
        sendResponse(exchange, response);
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
    private boolean isValidJson(JSONObject jsonObject) {
        return jsonObject.has("school")&&jsonObject.has("degree")&&jsonObject.has("fieldOfStudy")&&jsonObject.has("grade")&&jsonObject.has("activities")&&jsonObject.has("detail")&&jsonObject.has("startDate")&&jsonObject.has("endDate")&&jsonObject.has("workDetail")&&jsonObject.has("accessEdu")&&jsonObject.has("email");
    }
    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
        exchange.sendResponseHeaders(200, response.length());
    }
    private String handleGetRequest(HttpExchange exchange, EducationController educationController, String[] pathParts) throws SQLException, IOException {
        String response="";
        if(pathParts.length==3){
            if(pathParts[2].equals("all")){//contact/all
                response=educationController.getAllEducations();
                exchange.sendResponseHeaders(200, response.length());
            }else {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
            }
        }else if(pathParts.length==2){
            String email = JwtController.verifyToken(exchange);
            if(email!=null){
                response=educationController.getEducations(email);
                if (response == null) {
                    response = "Contact not found";
                    exchange.sendResponseHeaders(404, response.length());
                } else {
                    exchange.sendResponseHeaders(200, response.length());
                    return response;
                }
            }else {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            }
        }else{
            response="invalid request";
            exchange.sendResponseHeaders(400,response.length());
        }
        return response;
    }
    private String handlePostRequest(HttpExchange exchange,EducationController educationController,String[] pathParts) throws SQLException,IOException{
        String response="";
        if(pathParts.length==2){
            String email=JwtController.verifyToken(exchange);
            JSONObject jsonObject = getJsonObject(exchange);
            if (!isValidJson(jsonObject)) {
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
                return response;
            }
            if(email!=null){
                //dates are wrong
                educationController.createEducation(jsonObject.getString("school"),jsonObject.getString("degree"),jsonObject.getString("fieldOfStudy"),jsonObject.getDouble("grade"), jsonObject.getString("activities"), jsonObject.getString("detail"),null,null,jsonObject.getString("workDetail"), jsonObject.getString("accessEdu"),email );
                response = "Education added successfully";
                exchange.sendResponseHeaders(200, response.length());
            }
        }else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }
    private String handleDeleteRequest(HttpExchange exchange,EducationController educationController,String[] pathParts) throws SQLException,IOException{
        String response="";
        if(pathParts.length==2){
            String email=JwtController.verifyToken(exchange);
            if(email==null){
                response= "Unauthorized";
                exchange.sendResponseHeaders(401,response.length());
            }
            else {
                educationController.deleteEducationByEmail(email);
                response = "Education deleted successfully";
                exchange.sendResponseHeaders(200, response.length());
            }
        }else{
            response="invalid Request";
            exchange.sendResponseHeaders(400,response.length());
        }
        return response;
    }
    private String handlePutRequest(HttpExchange exchange,EducationController educationController,String[] pathParts) throws SQLException,IOException {
        String response="";
        if(pathParts.length==2){
            String email=JwtController.verifyToken(exchange);
            JSONObject jsonObject=getJsonObject(exchange);
            String school= jsonObject.getString("school");
            if(! isValidJson(jsonObject)){
                response = "Invalid request";
                exchange.sendResponseHeaders(400, response.length());
                return response;
            }
            if (email == null) {
                response = "Unauthorized";
                exchange.sendResponseHeaders(401, response.length());
            }else{
                if(educationController.getEducationByEmailAndSchool(email,school)!=null) {
                    //wrong in dates
                    educationController.updateEducation(jsonObject.getString("school"), jsonObject.getString("degree"), jsonObject.getString("fieldOfStudy"), jsonObject.getDouble("grade"), jsonObject.getString("activities"), jsonObject.getString("detail"), null, null, jsonObject.getString("workDetail"), jsonObject.getString("accessEdu"), email);
                    response = "Contact updated successfully";
                    exchange.sendResponseHeaders(200, response.length());
                }
                else {
                    response = "Contact not found";
                    exchange.sendResponseHeaders(404, response.length());
                }
            }
        }
        else {
            response = "Invalid request";
            exchange.sendResponseHeaders(400, response.length());
        }
        return response;
    }
}
