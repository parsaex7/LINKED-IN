package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FollowController {
    @FXML
    private VBox mainPane;
    @FXML
    private Label headerLabel;

    public void initialize() throws IOException {
        showFollows(ProfileView.stat);
        headerLabel.setText(ProfileView.stat);
    }
    public VBox createPersonBlock(String email, int number) {
        Label emailLabel = new Label(email);
        Label numberLabel = new Label(String.valueOf(number));

        emailLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: Comic Sans MS");
        numberLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 0 10px 0 0;");

        VBox personBlock = new VBox(new HBox(numberLabel, emailLabel));
        personBlock.setStyle("-fx-background-color: #181a1b; -fx-border-color: black; -fx-border-width: 2; -fx-padding: 10px;");
        personBlock.setPrefWidth(500);
        personBlock.setPrefHeight(40);

        return personBlock;
    }

    private void showFollows(String stat) throws IOException {
        if (otherProfileView.isAuth) {
            showFollowsForAuth(stat);
        } else {
            showFollowsForNonAuth(stat);
        }

    }

    private void showFollowsForNonAuth(String stat) throws IOException {
        URL url = new URL(Functions.getFirstOfUrl() + "follow/" + stat + "/" + otherProfileView.user.getEmail());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
        connection.setRequestMethod("GET");
        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            String response = Functions.getResponse(connection);
            List<String> emails = Functions.parseEmails(response);
            int i = 1;
            for (String email : emails) {
                mainPane.getChildren().add(createPersonBlock(email, i));
                i++;
            }
        } else {
            createPersonBlock("ERROR", 1);
        }
        connection.disconnect();
    }

    private void showFollowsForAuth(String stat) throws IOException {
        URL url = new URL(Functions.getFirstOfUrl() + "follow/" + stat);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
        connection.setRequestMethod("GET");
        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            String response = Functions.getResponse(connection);
            List<String> emails = Functions.parseEmails(response);
            int i = 1;
            for (String email : emails) {
                mainPane.getChildren().add(createPersonBlock(email, i));
                i++;
            }
        } else {
            createPersonBlock("ERROR", 1);
        }
        connection.disconnect();
    }

    public void backController(ActionEvent event) throws IOException {
        if (!otherProfileView.isAuth) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("otherProfile-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) headerLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) headerLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        }
    }
}
