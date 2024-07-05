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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
    @FXML
    private Label followerLabel;
    @FXML
    private Label followingLabel;

    public static String stat;

    public void postButtonController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("post-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) postButton.getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }

    @FXML
    public void initialize() throws IOException {
        displayUserInfo();
        displayFollowsCount("follower");
        displayFollowsCount("following");
        if (followerLabel.getText().equals("0")) {
            followerLabel.setOnMouseClicked(null);
        }
        if (followingLabel.getText().equals("0")) {
            followingLabel.setOnMouseClicked(null);
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

    public void homeController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
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

    public void followingController(MouseEvent event) throws IOException {
        stat = "following";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("follow-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }

    public void followerController(MouseEvent event) throws IOException {
        stat = "follower";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("follow-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }
    private void displayUserInfo() throws IOException {
        User user = LinkedInApplication.user;
        nameLabel.setText(user.getName() + " " + user.getLastName());
        profileGNAvatar.setImage(new Image(getClass().getResource("/org/example/assets/profile.jpg").toExternalForm()));
        if (user.getAdditionalName() != null) {
            additionLabel.setText(user.getAdditionalName());
            countryLabel.setText(user.getCountry() + ", " + user.getCity());
            //TODO: change hire and description label
        }
    }

    private void displayFollowsCount(String stat) throws IOException {
        URL url = new URL(Functions.getFirstOfUrl() + "follow/" + stat);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
        connection.setRequestMethod("GET");
        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            String response = Functions.getResponse(connection);
            List<String> emails = Functions.parseEmails(response);
            if (stat.equals("following")) {
                followingLabel.setText(String.valueOf(emails.size()));
            } else {
                followerLabel.setText(String.valueOf(emails.size()));
            }
        } else if (statusCode == 410) { //no follower
            if (stat.equals("following")) {
                followingLabel.setText("0");
            } else {
                followerLabel.setText("0");
            }
        } else {
            if (stat.equals("following")) {
                followingLabel.setText("ERROR");
            } else {
                followerLabel.setText("ERROR");
            }
        }
        connection.disconnect();
    }

}
