package org.example.client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
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
import org.example.model.Comment;
import org.example.model.Like;
import org.example.model.Post;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HashTagController {
    private static String toSearch;
    @FXML
    private VBox postContainer;
    @FXML
    private Button back;


    private Image likeImage = new Image(getClass().getResource("/org/example/assets/like.png").toExternalForm());
    private Image unlikeImage = new Image(getClass().getResource("/org/example/assets/unlike.png").toExternalForm());

    public static void setToSearch(String toSearch1) {
        toSearch = toSearch1;
    }
    public void onBack(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) back.getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void initialize(){
        if (!postContainer.getChildren().isEmpty()) {
            postContainer.getChildren().removeAll();
        }
        List<Post> posts = new ArrayList<>();
        List<Post> Validposts = new ArrayList<>();
        try {
            URL url=new URL(Functions.getFirstOfUrl()+"post/"+"all/"+"all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection.getResponseCode();
            if(statusCode==200){
                String response = Functions.getResponse(connection);
                posts=parsePosts(response);
                Validposts=getValidPosts(posts);
                for(Post post:Validposts){
                    postContainer.getChildren().add(createPostBlock(post));
                }
            }
            else{
                Post post=new Post("ERROR1","SERVER");
                postContainer.getChildren().add(createPostBlock(post));
            }

        }catch (Exception e){
            Post post=new Post("ERROR2","SERVER");
            postContainer.getChildren().add(createPostBlock(post));
        }
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
                        case "file_path":
                            post.setFile_path(parser.getText());
                            if (post.getFile_path().equals("null")) { post.setFile_path(null);}
                            break;
                    }
                }
                posts.add(post);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }
    private TextFlow createPostText(String text) {
        TextFlow textFlow = new TextFlow();
        textFlow.setStyle("-fx-text-fill: black; -fx-font-family: Comic Sans MS; -fx-font-size: 13px;");
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
        likeCount.setPadding(new Insets(0, 0, 0, 7));
        likeCount.setStyle("-fx-text-fill: black; -fx-font-family: Comic Sans MS; -fx-font-size: 11px;");
        VBox vBox = new VBox(like, likeCount);
        vBox.setCursor(Cursor.HAND);
        vBox.setSpacing(10);
        vBox.setOnMouseClicked(event -> handleLikeClick(postId, like, likeCount));
        updateLikeButtonStyle(postId, like, likeCount);
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
        comment.setCursor(Cursor.HAND);
        comment.setStyle(getClass().getResource("/org/example/assets/style.css").toExternalForm());
        comment.setOnMouseClicked(event -> handleCommentClick(postId));
        return comment;
    }

    private Label createShowLikes(int postId) {
        Label label = new Label("Show Likes");
        label.setStyle("-fx-text-fill: black; -fx-font-family: Comic Sans MS; -fx-font-size: 13px;");
        label.setCursor(Cursor.HAND);
        label.setOnMouseClicked(e -> handleShowLikes(postId));
        return label;
    }

    private Button createAddCommentButton(int postId) {
        Button comment = new Button("Add Comment");
        comment.setCursor(Cursor.HAND);
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

    public VBox createPostBlock(Post post) {
        VBox personBlock = new VBox();
        HBox hBox = new HBox(createLikeButton(post.getPostId()), createCommentButton(post.getPostId()), createShowLikes(post.getPostId()), createAddCommentButton(post.getPostId()));
        hBox.setSpacing(50);
        Label senderEmailLabel = new Label(post.getSenderEmail());
        senderEmailLabel.setStyle("-fx-text-fill: black; -fx-font-family: Comic Sans MS; -fx-font-size: 13px;");
        personBlock.getChildren().add(senderEmailLabel);
        personBlock.getChildren().add(createPostText(post.getMessage()));
        personBlock.getChildren().add(hBox);
        personBlock.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-spacing: 5px;");
        return personBlock;
    }
    List<Post> getValidPosts(List<Post> posts){
        List<Post> toreturn=new ArrayList<>();
        for(Post post:posts){
            if(post.getMessage().contains(toSearch)){
                toreturn.add(post);
            }
        }
        return toreturn;
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
//            e.printStackTrace();
        }
        for(Like like:likes){
            if(like.getUser_email().equals(LinkedInApplication.user.getEmail())){
                result=true;
                break;
            }
        }
        return result;
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
//            e.printStackTrace();
        }
        return likes.size();
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
}
