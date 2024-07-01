package org.example.client;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.regex.Pattern;

public class Functions {
    public static boolean patternMatches(String emailAddress) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static String getFirstOfUrl(){
        return "http://localhost:8000/";
    }

    public static String getResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputline;
        StringBuilder response = new StringBuilder();
        while ((inputline = in.readLine()) != null) {
            response.append(inputline);
        }
        in.close();
        return response.toString();
    }

    public static void sendResponse(HttpURLConnection connection, String response) throws IOException {
        connection.getOutputStream().write(response.getBytes());
        connection.getOutputStream().close();
    }

    public static void saveUser(String email, String password, String firstName, String lastName, String token) {
        LinkedInApplication.user = new User(email, password, token, firstName, lastName);
    }
    public static void saveUser(String email, String password, String firstName, String lastName, String country, String city, String additionalname, String token) {
        LinkedInApplication.user = new User(email, password, token, firstName, lastName, country, city, additionalname);
    }
    public static void buttonAnimation(Button createAccountButton) {
        createAccountButton.setDisable(true);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), createAccountButton);
        scaleTransition.setByX(0.1);
        scaleTransition.setByY(0.1);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }
    public static ScaleTransition getHeartbeat(Node node) {
        ScaleTransition heartbeat = new ScaleTransition(Duration.millis(75), node);
        heartbeat.setByX(0.15);
        heartbeat.setByY(0.15);
        heartbeat.setAutoReverse(true);
        heartbeat.setCycleCount(2);
        return heartbeat;
    }
    public static void fadeScene(Stage stage, Scene scene) {
        FadeTransition ftOut = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
        ftOut.setFromValue(1.0);
        ftOut.setToValue(0.0);

        ftOut.play();
        ftOut.setOnFinished(event ->
        {
            stage.setScene(scene);
            scene.getRoot().setOpacity(0);
            stage.show();
            FadeTransition ftIn = new FadeTransition(Duration.millis(500), scene.getRoot());
            ftIn.setFromValue(0.0);
            ftIn.setToValue(1.0);
            ftIn.play();
        });
    }
}
