package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchController {
    @FXML
    private Button back;
    @FXML
    private Button search;
    @FXML
    private VBox personContainer;
    @FXML
    private TextField searchTextField;
    @FXML
    private ScrollPane resultScrollPane;

    public void onSerach(){
        try {
            if(personContainer.getChildren().size()>0){
                personContainer.getChildren().clear();
            }
            URL url = new URL(Functions.getFirstOfUrl() + "search/" + searchTextField.getText());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection.getResponseCode();
            if (statusCode == 404) {
                //TODO: show user not found
            } else if (statusCode >= 400) {
                System.out.println("Server error");
                System.out.println(statusCode);
            } else {
                String response = Functions.getResponse(connection);
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
                });
                System.out.println(emails);
                for (String email : emails) {
                    if(!email.equals(LinkedInApplication.user.getEmail())) {
                        personContainer.getChildren().add(createPersonBlock(email));
                    }
                }
            }
        }catch (Exception e){
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

    @FXML
    public void resultClicked(String email) {
        searchTextField.setText(email);
    }
    public VBox createPersonBlock(String email){
        Label nameLabel = new Label(email);
        Button follow=new Button();
        if(isFollow(email)){
            follow.setText("Un follow");
        }
        else{
            follow.setText("Follow");
        }
        VBox personBlock = new VBox(nameLabel,follow);
        personBlock.setCursor(Cursor.CLOSED_HAND);
        personBlock.setOnMouseClicked(evnt->{
            resultClicked(email);
        });
        follow.setOnMouseClicked(mouseEvent -> {
            if(follow.getText().equals("Follow")){
                try {
                    URL url = new URL(Functions.getFirstOfUrl() + "follow/"+email);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                    int statusCode = connection.getResponseCode();
                    if(statusCode==200){
                        follow.setText("Un follow");
                    }
                    else{
                        follow.setText("ERROR1");
                    }
                }catch (Exception e){
                    follow.setText("ERROR2");
                    e.printStackTrace();
                }
            }
            else if(follow.getText().equals("Un follow")){
                try {
                    URL url = new URL(Functions.getFirstOfUrl() + "follow/"+email);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");
                    connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                    int statusCode = connection.getResponseCode();
                    if(statusCode==200){
                        follow.setText("Follow");
                    }
                    else{
                        follow.setText("ERROR1");
                    }
                }catch (Exception e){
                    follow.setText("ERROR2");
                    e.printStackTrace();
                }
            }
        });
        personBlock.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        return personBlock;
    }
    public boolean isFollow(String emailOfFollowing){
        List<String> emails=new ArrayList<>();
        try {
            URL url = new URL(Functions.getFirstOfUrl() + "follow"+"/"+"following");//follow/following
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            int statusCode = connection.getResponseCode();
            if (statusCode == 404) {
                //TODO: show user not found
            } else if (statusCode >= 400) {
                System.out.println("Server error");
                System.out.println(statusCode);
            } else {
                String response = Functions.getResponse(connection);
                ObjectMapper objectMapper = new ObjectMapper();
                emails = objectMapper.readValue(response, new TypeReference<List<String>>() {
                });
            }
        }catch (Exception e){
                e.printStackTrace();
        }
        if(emails.contains(emailOfFollowing)){
            return true;
        }
        else{
            return false;
        }
    }
}
