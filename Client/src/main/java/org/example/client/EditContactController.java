package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class EditContactController {
    @FXML
    private Button back;
    @FXML
    private TextField profileLink;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField numberType;
    @FXML
    private TextField address;
    @FXML
    private TextField contactId;
    @FXML
    private TextField birthdayAccess;
    @FXML
    private Label result;
    public void onEdit(){
        if((!birthdayAccess.getText().equalsIgnoreCase("me"))&&(!birthdayAccess.getText().equalsIgnoreCase("contacts"))&&(!birthdayAccess.getText().equalsIgnoreCase("everyone"))){
            result.setText("check the birthday access");
        }
            else if((!numberType.getText().equalsIgnoreCase("mobile"))&&(!numberType.getText().equalsIgnoreCase("home"))&&(!numberType.getText().equalsIgnoreCase("work"))){
            result.setText("check the number type");
        }
        else{
            try {
                URL url = new URL(Functions.getFirstOfUrl() + "contact");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                connection.setDoOutput(true);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("profilelink", profileLink.getText());
                jsonObject.put("phonenumber", phoneNumber.getText());
                jsonObject.put("numbertype",numberType.getText().toLowerCase());
                jsonObject.put("address",address.getText());
                jsonObject.put("contactid",contactId.getText());
                jsonObject.put("birthdayaccess",birthdayAccess.getText().toLowerCase());
                Functions.sendResponse(connection, jsonObject.toString());
                int statusCode = connection.getResponseCode();
                if(statusCode==200){
                    result.setText("Edited Successfully");
                }
                else{
                    result.setText("EROR 1");
                }
            }catch (Exception e){
                result.setText("EROR 2");
            }
        }
    }
    public void onBack(ActionEvent event) {
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
}
