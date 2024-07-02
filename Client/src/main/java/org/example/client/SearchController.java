package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SearchController {
    @FXML
    private Button search;
    @FXML
    private VBox personContainer;
    @FXML
    private TextField searchTextField;

    public void onSerach(){
        try {
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
                    personContainer.getChildren().add(createPersonBlock(email));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public VBox createPersonBlock(String email){
        Label nameLabel = new Label(email);
        VBox personBlock = new VBox(nameLabel);
        personBlock.setStyle("-fx-border-color: black; -fx-padding: 10px;");
        return personBlock;
    }
}
