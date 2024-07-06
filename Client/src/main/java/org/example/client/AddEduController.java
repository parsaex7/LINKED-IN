package org.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class AddEduController {
    @FXML
    private TextField school;
    @FXML
    private TextField degree;
    @FXML
    private TextField fieldofstudy;
    @FXML
    private TextField grade;
    @FXML
    private TextField detai;
    @FXML
    private TextField startdate;
    @FXML
    private TextField Enddate;
    @FXML
    private TextField workdetail;
    @FXML
    private TextField accessEdu;
    @FXML
    private Button add;
    public void onAdd(){
        try {
            URL url = new URL(Functions.getFirstOfUrl() + "education");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            connection.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("school", school.getText());
            jsonObject.put("degree",degree.getText());
            jsonObject.put("fieldofstudy",fieldofstudy.getText());
            jsonObject.put("grade",grade.getText());
            jsonObject.put("detail",detai.getText());
            jsonObject.put("startdate",startdate.getText());
            jsonObject.put("enddate",Enddate.getText());
            jsonObject.put("workdetail",workdetail.getText());
            jsonObject.put("accessedu",accessEdu.getText());
            Functions.sendResponse(connection, jsonObject.toString());
            int statusCode = connection.getResponseCode();
            String response=Functions.getResponse(connection);
            if (statusCode == 200) {
                //result.setText("comment added Successfully");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
                Stage stage = (Stage) add.getScene().getWindow();
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Functions.fadeScene(stage, scene);
            } else {
                System.out.println(statusCode);
                //result.setText("Internal Error");
            }
        }catch (Exception e){
            //result.setText("Server Error");
        }
    }
}
