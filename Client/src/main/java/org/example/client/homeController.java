package org.example.client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.example.model.Like;
import org.example.model.Post;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class homeController {
    @FXML
    private VBox postContainer;

    private Image likeImage = new Image(getClass().getResource("/org/example/assets/like.png").toExternalForm());
    private Image unlikeImage = new Image(getClass().getResource("/org/example/assets/unlike.png").toExternalForm());

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
                postContainer.getChildren().add(new Label("NO FOLLOWING"));
                return;
            } else if (statusCode >= 400) {
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
                    //nothing
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
            if (posts.isEmpty()) {
                postContainer.getChildren().add(new Label("NO POST"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private TextFlow createPostText(String text) {
        TextFlow textFlow = new TextFlow();
        textFlow.setStyle("-fx-text-fill: #ffffff; -fx-font-family: Comic Sans MS; -fx-font-size: 13px;");
        textFlow.setLineSpacing(5);
        textFlow.setPadding(new Insets(10, 0, 10, 5));
        String[] words = text.split("\\s+");
        for (String word : words) {
            Text wordText = new Text(word + " ");
            if (word.startsWith("#")) {
                wordText.setFill(Color.BLUE);
                wordText.setOnMouseClicked(event -> handleHashtagClick(word));
            } else {
                wordText.setFill(Color.WHITE);
            }
            textFlow.getChildren().add(wordText);
        }
        return textFlow;
    }

    private void handleHashtagClick(String word) {
        try {
            HashTagController.setToSearch(word);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HashTag-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) postContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createLikeButton(int postId) {
        ImageView like = new ImageView(unlikeImage);
        like.setCursor(Cursor.HAND);
        like.setFitHeight(20);
        like.setFitWidth(20);
        Label likeCount = new Label();
        like.setOnMouseClicked(event -> handleLikeClick(postId, like, likeCount));
        updateLikeButtonStyle(postId, like, likeCount);
        VBox vBox = new VBox(like, likeCount);
        vBox.setSpacing(10);
        return vBox;
    }

    private void handleLikeClick(int postId, ImageView like, Label likeCount) {
        if (like.getImage() == unlikeImage) {
            try {
                URL url = new URL(Functions.getFirstOfUrl() + "like/" + postId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                int statusCode = connection.getResponseCode();
                if (statusCode == 200) {
                    connection.disconnect();
                    like.setImage(likeImage);
                    int numberOfLikes = getNumberOfLikes(postId);
                    likeCount.setText(" " + String.valueOf(numberOfLikes));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL url = new URL(Functions.getFirstOfUrl() + "like/" + postId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                int statusCode = connection.getResponseCode();
                if (statusCode == 200) {
                    connection.disconnect();
                    like.setImage(unlikeImage);
                    int numberOfLikes = getNumberOfLikes(postId);
                    likeCount.setText(" " + String.valueOf(numberOfLikes));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Button createCommentButton(int postId) {
        Button comment = new Button("Comment");
        comment.setStyle(getClass().getResource("/org/example/assets/style.css").toExternalForm());
        comment.setOnMouseClicked(event -> handleCommentClick(postId));
        return comment;
    }

    private Label createShowLikes(int postId) {
        Label label = new Label("Show Likes");
        label.setStyle("-fx-text-fill: WHITE; -fx-font-family: Comic Sans MS; -fx-font-size: 13px;");
        label.setCursor(Cursor.HAND);
        label.setOnMouseClicked(e -> handleShowLikes(postId));
        return label;
    }

    private Button createAddCommentButton(int postId) {
        Button comment = new Button("Add Comment");
        comment.setStyle(getClass().getResource("/org/example/assets/style.css").toExternalForm());
        comment.setOnMouseClicked(event -> handleAddComment(postId));
        return comment;
    }

    private void handleAddComment(int postId) {
        AddCommentViewController.setPostId(postId);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addComment-view.fxml"));
            Stage stage = (Stage) postContainer.getScene().getWindow();
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleShowLikes(int postId) {
        try {
            likerViewController.setPostId(postId);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("liker-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) postContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCommentClick(int postId) {
        try {
            commentOfPostController.setPostId(postId);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("commentsOfPost-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) postContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLikeButtonStyle(int postId, ImageView like, Label likeCount) {
        int numberOfLikes = getNumberOfLikes(postId);
        if (isLikedBefore(postId)) {
            like.setImage(likeImage);
        } else {
            like.setImage(unlikeImage);
        }
        likeCount.setText(String.valueOf(numberOfLikes));
    }

    public VBox createPersonBlock(Post post) {
        VBox personBlock = new VBox();
        HBox hBox = new HBox(createLikeButton(post.getPostId()), createCommentButton(post.getPostId()), createShowLikes(post.getPostId()), createAddCommentButton(post.getPostId()));
        hBox.setSpacing(50);
        Label senderEmailLabel = new Label(post.getSenderEmail());
        senderEmailLabel.setStyle("-fx-text-fill: WHITE; -fx-font-family: Comic Sans MS; -fx-font-size: 13px;");
        personBlock.getChildren().add(senderEmailLabel);
        personBlock.getChildren().add(createPostText(post.getMessage()));
        personBlock.getChildren().add(hBox);
        personBlock.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-spacing: 5px;");
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

    public int getNumberOfLikes(int postId) {
        List<Like> likes = new ArrayList<>();
        try {
            URL url1 = new URL(Functions.getFirstOfUrl() + "like/" + postId);
            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection1.getResponseCode();
            if (statusCode == 200) {
                String response1 = Functions.getResponse(connection1);
                likes = parseLikes(response1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return likes.size();
    }

    public boolean isLikedBefore(int postId) {
        boolean result = false;
        List<Like> likes = new ArrayList<>();
        try {
            URL url1 = new URL(Functions.getFirstOfUrl() + "like/" + postId);
            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            String response1 = Functions.getResponse(connection1);
            likes = parseLikes(response1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Like like : likes) {
            if (like.getUser_email().equals(LinkedInApplication.user.getEmail())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void onBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) postContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSetting(ActionEvent event) {

    }

    public void searchButtonController(ActionEvent event) {
        try {
            otherProfileView.isAuth = true;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchUser-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void profileController(ActionEvent event) throws IOException {
        otherProfileView.isAuth = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }

    public void postController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("post-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) postContainer.getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }
}