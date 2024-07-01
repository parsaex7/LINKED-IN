package org.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.model.User;

import java.io.IOException;

public class LinkedInApplication extends Application {
    public static User user;
    public static Scene scene;
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LinkedInApplication.class.getResource("intro-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 950, 670);
            stage.setTitle("WELCOME");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error starting application");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}