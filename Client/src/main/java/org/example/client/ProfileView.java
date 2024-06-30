package org.example.client;

import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class ProfileView {
    @FXML
    private Button postButton;
    @FXML
    private Label nameLabel;
    @FXML
    private GNAvatarView profileGNAvatar;
    @FXML
    private Button logoutButton;

    public void postButtonController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("post-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) postButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        nameLabel.setText(LinkedInApplication.user.getName());
        profileGNAvatar.setImage(new Image(getClass().getResource("/org/example/assets/profile.jpg").toExternalForm()));
    }

    public void logoutButtonController(ActionEvent event) {
        try {
            File file = new File("src/main/resources/org/example/assets/token.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            LinkedInApplication.user = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("intro-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
