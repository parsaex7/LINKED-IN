package org.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class introViewController {
    @FXML
    private Button logIn;
    @FXML
    private Button signUp;
    public void onLogIn(){
        logIn.setOnMouseClicked( new EventHandler< MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                try{
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("login-view.fxml"));
                    Parent root=loader.load();
                    Stage stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene=new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void onSignUp(){
        signUp.setOnMouseClicked( new EventHandler< MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                try{
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("signup-view.fxml"));
                    Parent root=loader.load();
                    Stage stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene=new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
