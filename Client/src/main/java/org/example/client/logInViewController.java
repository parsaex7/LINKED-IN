package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class logInViewController {
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Label resultLabel;
    @FXML
    private Button signUpButton;

    public void loginButtonClicked() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        try {
            if (!Functions.patternMatches(email)) {
                resultLabel.setText("Invalid email");
            } else if (password.length() < 8) {
                resultLabel.setText("Invalid password");
            } else {
                //http://localhost:8000/login/email/password
                URL url = new URL(Functions.getFirstOfUrl() + "login/" + emailTextField.getText() + "/" + passwordTextField.getText());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int statusCode = connection.getResponseCode();
                if (statusCode == 404) { //user not found
                    resultLabel.setText("User not found");
                } else if (statusCode >= 400) {
                    resultLabel.setText("Server error");
                } else {
                    resultLabel.setText("Login successful");
                    String response = Functions.getResponse(connection);
                    Functions.saveUser(email, password, null, null, response);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } catch (Exception e) {
            resultLabel.setText("connection failed");
            e.printStackTrace();
        }
    }

    public void signUpButtonController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
