package org.example.client;

import io.github.gleidson28.GNAvatarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.User;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    @FXML
    private Button setting;
    @FXML
    private Button postButton;
    @FXML
    private Button searchButton;
    @FXML
    private VBox mainPane;
    @FXML
    private Button back;
    @FXML
    private TextField searchTextField;
    @FXML
    private ScrollPane resultScrollPane;

    public void onSearch() {
        try {
            if (!mainPane.getChildren().isEmpty()) {
                mainPane.getChildren().clear();
            }
            URL url = new URL(Functions.getFirstOfUrl() + "search/" + searchTextField.getText());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection.getResponseCode();
            if (statusCode == 404) {
                mainPane.getChildren().add(createNoUserFoundBlock());
            } else if (statusCode >= 400) {
                System.out.println("Server error");
            } else {
                String response = Functions.getResponse(connection);
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
                });
                System.out.println(emails);
                for (String email : emails) {
                    if (!email.equals(LinkedInApplication.user.getEmail())) {
                        mainPane.getChildren().add(createPersonBlock(email));
                    }
                }
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backController(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
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
        Stage stage = (Stage) mainPane.getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }


    public VBox createPersonBlock(String email) {
        Label nameLabel = new Label(email);
        Button follow = new Button();
        follow.setPrefWidth(100);
        nameLabel.setPrefWidth(200);
        nameLabel.setCursor(Cursor.HAND);


        if (isFollow(email)) {
            follow.setText("Unfollow");
            follow.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-family: Comic Sans MS;");
        } else {
            follow.setText("Follow");
            follow.setStyle("-fx-background-color: #32cd32; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-family: Comic Sans MS;");
        }

        HBox hbox = new HBox(nameLabel, follow);
        hbox.setSpacing(200);
        hbox.setAlignment(Pos.CENTER_LEFT);

        VBox personBlock = new VBox(hbox);
        nameLabel.setOnMouseClicked(event -> {
            try {
                otherProfileView.user = getUser(nameLabel.getText());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("otherProfile-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                Functions.fadeScene(stage, scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        follow.setOnMouseClicked(mouseEvent -> {
            if (follow.getText().equals("Follow")) {
                follow.setText("Unfollow");
                follow.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white;");
                try {
                    followLogic(nameLabel.getText(), "follow");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (follow.getText().equals("Unfollow")) {
                follow.setText("Follow");
                follow.setStyle("-fx-background-color: #32cd32; -fx-text-fill: white;");
                try {
                    followLogic(nameLabel.getText(), "unfollow");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        personBlock.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        hbox.setFillHeight(true);

        return personBlock;
    }

    public boolean isFollow(String emailOfFollowing) {
        List<String> emails = new ArrayList<>();
        try {
            URL url = new URL(Functions.getFirstOfUrl() + "follow" + "/" + "following");//follow/following
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection.getResponseCode();
            if (statusCode == 404) {

            } else if (statusCode >= 400) {
                System.out.println("Server error");
            } else {
                String response = Functions.getResponse(connection);
                connection.disconnect();
                ObjectMapper objectMapper = new ObjectMapper();
                emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (emails.contains(emailOfFollowing)) {
            return true;
        } else {
            return false;
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

    public void postButtonController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("post-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) postButton.getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }

    public void onSetting(ActionEvent event) {

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

    public VBox createNoUserFoundBlock() {
        VBox noUserBlock = new VBox();
        noUserBlock.setAlignment(Pos.CENTER);
        noUserBlock.setPrefHeight(100);
        noUserBlock.setPrefWidth(500);

        Label messageLabel = new Label("No user found.");
        messageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Comic Sans MS';");

        noUserBlock.getChildren().add(messageLabel);
        noUserBlock.setStyle("-fx-background-color: #181a1b; -fx-border-color: black; -fx-border-width: 2; -fx-padding: 20px;");

        return noUserBlock;
    }

    private void followLogic(String email, String cmd) throws IOException {
        URL url = new URL(Functions.getFirstOfUrl() + "follow/" + email);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
        if (cmd.equals("follow")) {
            connection.setRequestMethod("POST");
        } else {
            connection.setRequestMethod("DELETE");
        }
        int statusCode = connection.getResponseCode();
        if (statusCode != 200) {
            System.out.println("error in follow");
        }
        connection.disconnect();
    }

    private User getUser(String email) throws IOException {
        URL url = new URL(Functions.getFirstOfUrl() + "user/" + email);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            String response = Functions.getResponse(connection);
            JSONObject jsonObject = new JSONObject(response);
            String name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
            String lastName = jsonObject.isNull("lastName") ? null : jsonObject.getString("lastName");
            String country = jsonObject.isNull("country") ? null : jsonObject.getString("country");
            String city = jsonObject.isNull("city") ? null : jsonObject.getString("city");
            String additionalName = jsonObject.isNull("additionalName") ? null : jsonObject.getString("additionalName");
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setLastName(lastName);
            user.setCountry(country);
            user.setCity(city);
            user.setAdditionalName(additionalName);
            return user;
        } else {
            System.out.println("Error in getting user");
            throw new RuntimeException();
        }
    }
}
