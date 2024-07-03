package org.example.client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.model.Post;

import java.io.IOException;
import java.net.HttpURLConnection;
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
        Label message = new Label(post.getMessage());
        VBox personBlock = new VBox(nameLabel, message);
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
}
