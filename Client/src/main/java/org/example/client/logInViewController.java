package org.example.client;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.GlassPane;
import org.json.JSONObject;

import java.io.*;
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
    @FXML
    private StackPane mainPane;
    @FXML
    private Label welcomeLabel;


    private GlassPane glassPane;

    public void loginButtonClicked() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        try {
            if (!Functions.patternMatches(email)) {
                resultLabel.setText("Invalid email");
            } else if (password.length() < 8) {
                resultLabel.setText("Invalid password");
            } else {
                Functions.buttonAnimation(loginButton);
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
                    String response = Functions.getResponse(connection);
                    JSONObject jsonObject = new JSONObject(response);
                    String token = connection.getHeaderField("JWT");
                    String name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
                    String lastName = jsonObject.isNull("lastName") ? null : jsonObject.getString("lastName");
                    String country = jsonObject.isNull("country") ? null : jsonObject.getString("country");
                    String city = jsonObject.isNull("city") ? null : jsonObject.getString("city");
                    String additionalName = jsonObject.isNull("additionalName") ? null : jsonObject.getString("additionalName");
                    Functions.saveUser(email, password, name, lastName, country, city, additionalName, token);


                    //save token for next time login
                    try {
                        File file = new File("src/main/resources/org/example/assets/token.txt");
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(token);
                        fileWriter.close();
                    } catch (IOException e) {
                        System.out.println("error in saving token");
                        e.printStackTrace();
                    }

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    Functions.fadeScene(stage, scene);
                }
            }
        } catch (Exception e) {
            resultLabel.setText("connection failed");
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        try {
            glassPane = new GlassPane();
            glassPane.setPickOnBounds(false);
            mainPane.getChildren().add(glassPane);
            loginButton.setMouseTransparent(false);
            signUpButton.setMouseTransparent(false);
            passwordTextField.setMouseTransparent(false);
            emailTextField.setMouseTransparent(false);
            welcomeLabel.setOpacity(0);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), welcomeLabel);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);
            fadeIn.setAutoReverse(false);
            fadeIn.play();

            File file = new File("src/main/resources/org/example/assets/token.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String token = bufferedReader.readLine();
            if (token != null) {
                URL url = new URL(Functions.getFirstOfUrl() + "login");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("JWT", token);
                int statusCode = connection.getResponseCode();
                if (statusCode == 200) {
                    String response = Functions.getResponse(connection);
                    JSONObject jsonObject = new JSONObject(response);
                    String name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
                    String lastName = jsonObject.isNull("lastName") ? null : jsonObject.getString("lastName");
                    String country = jsonObject.isNull("country") ? null : jsonObject.getString("country");
                    String city = jsonObject.isNull("city") ? null : jsonObject.getString("city");
                    String additionalName = jsonObject.isNull("additionalName") ? null : jsonObject.getString("additionalName");
                    Functions.saveUser(jsonObject.getString("email"), jsonObject.getString("password"), name, lastName, country, city, additionalName, token);    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
                            Parent root = loader.load();
                            Stage stage = (Stage) LinkedInApplication.scene.getWindow();
                            Scene scene = new Scene(root);
                            Functions.fadeScene(stage, scene);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (Exception e) {
            System.out.println("connection failed");
            e.printStackTrace();
        }
    }

    public void signUpButtonController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }
}
