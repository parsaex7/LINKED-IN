package org.example.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Follow {
    @FXML
    private VBox followers;
    @FXML
    private VBox followings;
    private void onFollower(){
        try {

            URL url = new URL(Functions.getFirstOfUrl() + "follow/" + "follower");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection.getResponseCode();
            if (statusCode == 404) {
                //TODO: show user not found
                createPersonBlock("Not Found");
            } else if (statusCode >= 400) {
                System.out.println("Server error");
                System.out.println(statusCode);
            } else {
                String response = Functions.getResponse(connection);
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
                });
                System.out.println(emails);
                for (String email : emails) {
                    followers.getChildren().add(createPersonBlock(email));
                }
            }
        }catch (Exception e){
                createPersonBlock("Server Error");
        }
    }
    public void onFollowing(){
        try {

            URL url = new URL(Functions.getFirstOfUrl() + "follow/" + "following");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection.getResponseCode();
            if (statusCode == 404) {
                //TODO: show user not found
                createPersonBlock("Not Found");
            } else if (statusCode >= 400) {
                createPersonBlock("Server Error");
                System.out.println("Server error");
                System.out.println(statusCode);
            } else {
                String response = Functions.getResponse(connection);
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
                });
                System.out.println(emails);
                for (String email : emails) {
                    followings.getChildren().add(createPersonBlock(email));
                }
            }
        }catch (Exception e){
            createPersonBlock("Server Error");
        }
    }
    public VBox createPersonBlock(String email){
        Label nameLabel = new Label(email);
        VBox personBlock = new VBox(nameLabel);
        personBlock.setCursor(Cursor.CLOSED_HAND);
        personBlock.setOnMouseClicked(evnt->{
//            TODO:move to profile page
        });
        personBlock.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        return personBlock;
    }
}
