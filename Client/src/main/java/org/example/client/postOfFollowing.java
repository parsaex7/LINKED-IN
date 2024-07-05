package org.example.client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.example.model.Like;
import org.example.model.Post;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class postOfFollowing {
    @FXML
    private VBox postContainer;

    public void initialize() {
        List<String> emails = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        try {

            URL url = new URL(Functions.getFirstOfUrl() + "follow/" + "following");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection.getResponseCode();
            if (statusCode == 404) {
                //TODO: show user not found
//                createPersonBlock("Not Found");
            } else if (statusCode >= 400) {
//                createPersonBlock("Server Error");
                System.out.println("Server error");
                System.out.println(statusCode);
            } else {
                String response = Functions.getResponse(connection);
                ObjectMapper objectMapper = new ObjectMapper();
                emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
                });
            }
        } catch (Exception e) {
//            createPersonBlock("Server Error");
        }
        try {
            for (String email : emails) {
                URL url = new URL(Functions.getFirstOfUrl() + "post/" + "all/" + email);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                int statusCode = connection.getResponseCode();
                if (statusCode == 404) {
                    //TODO:no post
                } else if (statusCode >= 400) {
                    //TODO:Server error
                } else {
                    String response = Functions.getResponse(connection);
                    posts = parsePosts(response);
                }
            }
            for (Post post : posts) {
                postContainer.getChildren().add(createPersonBlock(post));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VBox createPersonBlock(Post post) {
        Label nameLabel = new Label(post.getSenderEmail());
        TextFlow textFlow=new TextFlow();
        String text=post.getMessage();
        String [] words=text.split("\\s+");
        for (String word : words) {
            Text wordText = new Text(word + " ");
            if (word.startsWith("#")) {
                wordText.setFill(Color.BLUE);
                // Set an action for hashtags (e.g., open a new window)
                wordText.setOnMouseClicked(event -> {
                    //TODO:NOW
                    try {
                        HashTagController.setToSearch(word);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("HashTag-view.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) postContainer.getScene().getWindow();
                        Scene scene = new Scene(root);
                        Functions.fadeScene(stage, scene);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
            textFlow.getChildren().add(wordText);
        }
        Label message = new Label(post.getMessage());
        Button like=new Button("LIKE");
        Button coment=new Button("Coment");
        Label showLikes=new Label("Show Likes");
        Label showComments=new Label("Show Comments");
        VBox personBlock = new VBox(nameLabel, textFlow,like,coment,showLikes,showComments);
        if(isLikedBefore(post.getPostId())){
            like.setStyle("-fx-background-color: RED");
            like.setText("LIKED:"+getNumberOfLikes(post.getPostId()));
        }
        else {
            like.setText("LIKES:"+getNumberOfLikes(post.getPostId()));
        }
        showComments.setOnMouseClicked(mouseEvent -> {
            try {
                commentOfPostController.setPostId(post.getPostId());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("commentsOfPost-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) coment.getScene().getWindow();
                Scene scene = new Scene(root);
                Functions.fadeScene(stage, scene);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        showLikes.setOnMouseClicked(mouseEvent -> {
                try {
                    likerViewController.setPostId(post.getPostId());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("liker-view.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) showLikes.getScene().getWindow();
                    Scene scene = new Scene(root);
                    Functions.fadeScene(stage, scene);
                }catch (Exception e){
                    e.printStackTrace();
                }
        });
        like.setOnMouseClicked(mouseEvent -> {
                if (!like.getText().startsWith("LIKED")) {
                try {
                    URL url = new URL(Functions.getFirstOfUrl() + "like/" + post.getPostId());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                    int statusCode = connection.getResponseCode();
                    if (statusCode == 200) {
                        connection.disconnect();
                        like.setStyle("-fx-background-color: RED");
                        like.setText("LIKED:"+getNumberOfLikes(post.getPostId()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    URL url = new URL(Functions.getFirstOfUrl() + "like/" + post.getPostId());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");
                    connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                    int statusCode = connection.getResponseCode();
                    if (statusCode == 200) {
                        connection.disconnect();
                        like.setStyle("-fx-background-color: White");
                        like.setText("LIKES:"+getNumberOfLikes(post.getPostId()));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        coment.setOnMouseClicked(mouseEvent -> {
                AddCommentViewController.setPostId(post.getPostId());
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("addComment-view.fxml"));
                    Stage stage = (Stage) coment.getScene().getWindow();
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Functions.fadeScene(stage, scene);
                }catch (Exception e){
                    e.printStackTrace();
                }

        });
        personBlock.setCursor(Cursor.CLOSED_HAND);
        personBlock.setOnMouseClicked(evnt -> {
//            TODO:move to profile page
        });
        personBlock.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        return personBlock;
    }

    public List<Post> parsePosts(String jsonResponse) {
        List<Post> posts = new ArrayList<>();
        JsonFactory factory = new JsonFactory();
        try (JsonParser parser = factory.createParser(jsonResponse)) {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected an array");
            }
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                Post post = new Post();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken(); // move to value
                    switch (fieldName) {
                        case "message":
                            post.setMessage(parser.getText());
                            break;
                        case "postId":
                            post.setPostId(parser.getIntValue());
                            break;
                        case "senderEmail":
                            post.setSenderEmail(parser.getText());
                    }
                }
                posts.add(post);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }
    public List<Like> parseLikes(String jsonResponse) {
        List<Like> likes = new ArrayList<>();
        JsonFactory factory = new JsonFactory();
        try (JsonParser parser = factory.createParser(jsonResponse)) {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected an array");
            }
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                Like like = new Like();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken(); // move to value
                    switch (fieldName) {
                        case "post_id":
                            like.setPost_id(parser.getIntValue());
                            break;
                        case "user_email":
                            like.setUser_email(parser.getText());
                            break;
                    }
                }
                likes.add(like);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return likes;
    }
    public int getNumberOfLikes(int postId){
        List<Like> likes=new ArrayList<>();
        try {
            URL url1 = new URL(Functions.getFirstOfUrl() + "like/" + postId);
            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            String response1 = Functions.getResponse(connection1);
            likes = parseLikes(response1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return likes.size();
    }
    public boolean isLikedBefore(int postId){
        boolean result=false;
        List<Like> likes=new ArrayList<>();
        try {
            URL url1 = new URL(Functions.getFirstOfUrl() + "like/" + postId);
            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            String response1 = Functions.getResponse(connection1);
            likes = parseLikes(response1);
        }catch (Exception e){
            e.printStackTrace();
        }
        for(Like like:likes){
            if(like.getUser_email().equals(LinkedInApplication.user.getEmail())){
                result=true;
                break;
            }
        }
        return result;
    }
    public void onBackButtonPressed(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) postContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}