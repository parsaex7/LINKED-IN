package org.example.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.model.Post;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class postOfFollowing {
    @FXML
    private VBox postContainer;
    public void ShowPost(){
        List<String> emails=new ArrayList<>();
        List<Post>  posts=new ArrayList<>();
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
                emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
                });
            }
        }catch (Exception e){
            createPersonBlock("Server Error");
        }
        try {
            for(String email:emails){
                URL url=new URL(Functions.getFirstOfUrl()+"post/"+"all");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                int statusCode = connection.getResponseCode();
                if(statusCode==404){
                    //TODO:no post
                }
                else if(statusCode>=400){
                    //TODO:Server error
                }
                else{
                    String response = Functions.getResponse(connection);
                    ObjectMapper objectMapper = new ObjectMapper();
                   List<Post> postofThisUser = objectMapper.readValue(response, new TypeReference<List<Post>>() {
                    });
                   for(Post post:postofThisUser){
                       posts.add(post);
                   }
                }
            }
            for(Post post:posts){
                postContainer.getChildren().add(createPersonBlock(post.getMessage()));
            }

        }catch (Exception e){
            e.printStackTrace();
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
