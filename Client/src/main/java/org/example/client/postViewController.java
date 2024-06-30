package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class postViewController {
    @FXML
    private Button back;
    @FXML
    private TextArea textArea;
    @FXML
    private Button post;
    @FXML
    private Label result;

    public void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onPost() {
        try {
            URL url = new URL(Functions.getFirstOfUrl() + "post");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            connection.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", textArea.getText());
            Functions.sendResponse(connection, jsonObject.toString());
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                result.setText("Posted Successfully");
                Thread.sleep(5000);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
                Stage stage = (Stage) textArea.getScene().getWindow();
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                result.setText("EROR1");
            }
        }catch (Exception e){
            result.setText("EROR2");
        }
    }
}
