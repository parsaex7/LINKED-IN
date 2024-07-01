package org.example.client;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.GlassPane;

public class introViewController {
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private VBox blurredVbox;
    @FXML
    private StackPane mainPane;
    @FXML
    private Label welcomeLabel;

    private GlassPane glassPane;

    public void loginController(ActionEvent event) {
        try {
            Functions.buttonAnimation(loginButton);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signupController(ActionEvent event) {
        try {
            Functions.buttonAnimation(signupButton);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        glassPane = new GlassPane();
        glassPane.setPickOnBounds(false);
        mainPane.getChildren().add(glassPane);
        blurredVbox.setMouseTransparent(false);
        loginButton.setMouseTransparent(false);
        signupButton.setMouseTransparent(false);
        welcomeLabel.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(4), welcomeLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.setAutoReverse(false);
        fadeIn.play();
    }
}
