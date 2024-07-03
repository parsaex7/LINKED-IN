package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.Contact;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class EditContactController {
    @FXML
    private Button backButton;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private ChoiceBox<String> numberTypeChoiceBox;
    @FXML
    private TextField addressTextField;
    @FXML
    private ChoiceBox<String> accessChoiceBox;
    @FXML
    private TextField contactIdTextField;
    @FXML
    private Label result;

    private Contact contact;

    public void initialize() throws IOException {
        accessChoiceBox.getItems().add("ME");
        accessChoiceBox.getItems().add("EVERYONE");
        accessChoiceBox.getItems().add("CONTACTS");
        numberTypeChoiceBox.getItems().add("MOBILE");
        numberTypeChoiceBox.getItems().add("WORK");
        numberTypeChoiceBox.getItems().add("HOME");
        //http://localhost:8000/contact
        URL url = new URL(Functions.getFirstOfUrl() + "contact");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
        connection.setRequestMethod("GET");
        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            String response = Functions.getResponse(connection);
            if (!response.equals("null")) {
                JSONObject jsonObject = new JSONObject(response);
                String phoneNumber = jsonObject.isNull("phoneNumber") ? "" : jsonObject.getString("phoneNumber");
                String numberType = jsonObject.isNull("numberType") ? "" : jsonObject.getString("numberType");
                String address = jsonObject.isNull("address") ? "" : jsonObject.getString("address");
                String contactId = jsonObject.isNull("contactId") ? "" : jsonObject.getString("contactId");
                String birthDayAccess = jsonObject.isNull("birthdayAccess") ? "" : jsonObject.getString("birthdayAccess");
                contact = new Contact(phoneNumber, numberType, address, contactId, birthDayAccess);
                phoneNumberTextField.setText(phoneNumber);
                addressTextField.setText(address);
                contactIdTextField.setText(contactId);
                accessChoiceBox.setValue(birthDayAccess);
                numberTypeChoiceBox.setValue(numberType.toUpperCase());
            }
        } else {
            result.setText("ERROR");
            System.out.println(statusCode);
        }
    }

    public void editController() {
        if (addressTextField.getText().isEmpty() || phoneNumberTextField.getText().isEmpty() || accessChoiceBox.getValue().isEmpty() || numberTypeChoiceBox.getValue().isEmpty() || contactIdTextField.getText().isEmpty()) {
            result.setText("Please Fill All of The Blanks");
        } else {
            try {
                URL url = new URL(Functions.getFirstOfUrl() + "contact");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("profilelink", "");
                jsonObject.put("phonenumber", phoneNumberTextField.getText());
                jsonObject.put("numbertype", numberTypeChoiceBox.getValue().toLowerCase());
                jsonObject.put("address", addressTextField.getText());
                jsonObject.put("contactid", contactIdTextField.getText());
                jsonObject.put("birthdayaccess", accessChoiceBox.getValue().toLowerCase());
                connection.setDoOutput(true);
                Functions.sendResponse(connection, jsonObject.toString());
                int statusCode = connection.getResponseCode();
                if (statusCode == 200) {
                result.setText("Updated Successfully");
                } else {
                    result.setText("error");
                }
            } catch (Exception e) {
                result.setText("ERROR");
                e.printStackTrace();
            }
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
}
