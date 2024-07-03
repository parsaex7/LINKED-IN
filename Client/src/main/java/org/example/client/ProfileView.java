package org.example.client;

import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class ProfileView {
    @FXML
    private Button setting;
    @FXML
    private Button postButton;
    @FXML
    private Label nameLabel;
    @FXML
    private GNAvatarView profileGNAvatar;
    @FXML
    private Button logoutButton;
    @FXML
    private Button searchButton;
    @FXML
    private VBox mainPane;
    @FXML
    private Label additionLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label hireLabel;
    @FXML
    private Label descriptionLabel;

    public void postButtonController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("post-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) postButton.getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }

    @FXML
    public void initialize() {
        User user = LinkedInApplication.user;
        nameLabel.setText(user.getName() + " " + user.getLastName());
        profileGNAvatar.setImage(new Image(getClass().getResource("/org/example/assets/profile.jpg").toExternalForm()));
        if (user.getAdditionalName() != null) {
            additionLabel.setText(user.getAdditionalName());
            countryLabel.setText(user.getCountry() + ", " + user.getCity());
            //TODO: change hire and description label
        }

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
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onSetting(ActionEvent event){

    }

    public void searchButtonController(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchUser-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("postOfFollowing-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Functions.fadeScene(stage, scene);
    }

    public void contactInfoController(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditContact.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void educationInfoController(MouseEvent mouseEvent) {
    }
}
