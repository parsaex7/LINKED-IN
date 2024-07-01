package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.stage.Stage;
import org.json.JSONObject;
public class signUpController {

    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private Button signupButton;
    @FXML
    private Label resultLabel;
    @FXML
    private Button loginButton;

    public void signupController(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        if (!Functions.patternMatches(email)) {
            resultLabel.setText("Invalid email");
        } else if (password.length() < 8) {
            resultLabel.setText("Invalid password. At least 8 Character");
        } else {
            try {
                URL url = new URL(Functions.getFirstOfUrl() + "user");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                JSONObject json = new JSONObject();
                json.put("firstname",firstNameTextField.getText());
                json.put("lastname",lastNameTextField.getText());
                json.put("email", email);
                json.put("password",passwordTextField.getText());

                //write to server and read response
                Functions.sendResponse(connection, json.toString());
                int statusCode = connection.getResponseCode();

                //check response
                if (statusCode == 409) {
                    resultLabel.setText("Email already Exists");
                } else if (statusCode >= 400) {
                    resultLabel.setText("Internal Error");
                } else {
                    resultLabel.setText("Signup successful");
                    String response = Functions.getResponse(connection);
                    Functions.saveUser(email, password, firstNameTextField.getText(), lastNameTextField.getText(), response);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    Functions.fadeScene(stage, scene);
                }
            } catch (Exception e) {
                resultLabel.setText("Internal Error");
                e.printStackTrace();
            }
        }
    }

    public void loginButtonController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }
}
