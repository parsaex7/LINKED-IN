package org.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class AddCommentViewController {
    private static int postId;
    @FXML
    private Button backButton;
    @FXML
    private Label result;
    @FXML
    private Button commentButton;
    @FXML
    private TextArea commentText;
    public void onCommentButton(){
        try {
            URL url = new URL(Functions.getFirstOfUrl() + "comment/"+postId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            connection.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("comment", commentText.getText());
            Functions.sendResponse(connection, jsonObject.toString());
            int statusCode = connection.getResponseCode();
            String response=Functions.getResponse(connection);
            if (statusCode == 200) {
                result.setText("comment added Successfully");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                Stage stage = (Stage) commentText.getScene().getWindow();
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Functions.fadeScene(stage, scene);
            } else {
                System.out.println(statusCode);
                result.setText("Internal Error");
            }
        }catch (Exception e){
            result.setText("Server Error");
        }
    }
    public void onBackButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Stage stage = (Stage) commentText.getScene().getWindow();
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        }catch (Exception e){
            result.setText("ERROR");
        }
    }

    public static void setPostId(int postId1) {
        postId = postId1;
    }
}
