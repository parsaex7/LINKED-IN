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
import java.util.*;

import java.net.HttpURLConnection;
import java.net.URL;

public class EditEduController {
    @FXML
    private Button back;
    @FXML
    private Button edit;
    @FXML
    private TextField school;
    @FXML
    private TextField degree;
    @FXML
    private TextField fieldOfStudy;
    @FXML
    private TextField grade;
    @FXML
    private TextField detail;
    @FXML
    private TextField startDate;
    @FXML
    private TextField endDate;
    @FXML
    private TextField workDetail;
    @FXML
    private TextField accessEdu;
    @FXML
    private Label result;
    public void onEdit(){
        if((endDate.getText().length()!=0)&&(!Functions.datePattern(endDate.getText()))){
            result.setText("check the end date");
        }
        else if((startDate.getText().length()!=0)&&(!Functions.datePattern(startDate.getText()))){
            result.setText("check the start date");
        }
         if((!accessEdu.getText().equalsIgnoreCase("me"))&&(! accessEdu.getText().equalsIgnoreCase("everyone"))&&(!accessEdu.getText().equalsIgnoreCase("contacts"))){
            result.setText("check the access education");
        }
        else{
            try {
                URL url = new URL(Functions.getFirstOfUrl() + "education");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
                connection.setDoOutput(true);
                JSONObject jsonObject = new JSONObject();
                String[] startParts=startDate.getText().split("/");
                String[] endParts=endDate.getText().split("/");
                int startYear=Integer.parseInt(startParts[0]);
                int startMonth=Integer.parseInt(startParts[1]);
                int startDay=Integer.parseInt(startParts[2]);
                int endYear=Integer.parseInt(endParts[0]);
                int endMonth=Integer.parseInt(endParts[1]);
                int endDay=Integer.parseInt(endParts[2]);
                jsonObject.put("school", school.getText());
                jsonObject.put("degree",degree.getText());
                jsonObject.put("fieldofstudy",fieldOfStudy.getText());
                jsonObject.put("grade",grade.getText());
                jsonObject.put("detail",detail.getText());
                jsonObject.put("startdate",new Date(startYear,startMonth,startDay));
                jsonObject.put("enddate",new Date(endYear,endMonth,endDay));
                jsonObject.put("workdetail",workDetail.getText());
                jsonObject.put("accessedu",accessEdu.getText());
                Functions.sendResponse(connection, jsonObject.toString());
                int statusCode = connection.getResponseCode();
                if(statusCode==200){
                    result.setText("edited");
                    //previous page
                }
                else{
                    result.setText("EROR1");
                }
            }catch (Exception e){
                    result.setText("EROR2");
            }
        }
    }
    public void onback(ActionEvent event) {
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
