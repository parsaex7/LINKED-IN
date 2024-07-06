package org.example.client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.Comment;
import org.example.model.Like;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class commentOfPostController {
    List<Comment> comments=new ArrayList<>();
    @FXML
    private Button back;
    @FXML
    private VBox personContainer;
    private static int postId;

    public static void setPostId(int postId1) {
      postId = postId1;
    }

    public void initialize(){
        try {
            URL url1 = new URL(Functions.getFirstOfUrl() + "comment"+"/"+"all/" + postId);//comment/all/post_id
            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            String response1 = Functions.getResponse(connection1);
             comments= parseComments(response1);
            for (Comment comment:comments) {
                personContainer.getChildren().add(createPersonBlock(comment));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public VBox createPersonBlock(Comment comment){
        Label nameLabel = new Label(comment.getUser_email());
        Label comentLabel=new Label(comment.getComment());
        VBox personBlock = new VBox(nameLabel,comentLabel);
        personBlock.setCursor(Cursor.CLOSED_HAND);
        personBlock.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        return personBlock;
    }
    public List<Comment> parseComments(String jsonResponse) {//check this method
        List<Comment> comments = new ArrayList<>();
        JsonFactory factory = new JsonFactory();
        try (JsonParser parser = factory.createParser(jsonResponse)) {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected an array");
            }
            while (parser.nextToken() != JsonToken.END_ARRAY) {
               Comment comment=new Comment();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken(); // move to value
                    switch (fieldName) {
                        case "post_id":
                            comment.setPost_id(parser.getIntValue());
                            break;
                        case "user_email":
                            comment.setUser_email(parser.getText());
                            break;
                        case "comment":
                            comment.setComment(parser.getText());
                            break;
                    }
                }
                comments.add(comment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comments;
    }
    public void onBack(){
        try {
            if (SearchPostController.onPostSearch) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("searchPost-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) back.getScene().getWindow();
                Scene scene = new Scene(root);
                Functions.fadeScene(stage, scene);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) back.getScene().getWindow();
                Scene scene = new Scene(root);
                Functions.fadeScene(stage, scene);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
