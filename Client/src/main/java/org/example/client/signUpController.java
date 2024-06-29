package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
public class signUpController {
    @FXML
    private TextField email;
    @FXML
    private TextField passWord;
    @FXML
    private TextField fristName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField country;
    @FXML
    private TextField city;
    @FXML
    private TextField additionalname;
    @FXML
    private Button signUp;
    @FXML
    private Label result;
    public void onSignUp(){
        if((email.getText().equals(""))||(passWord.getText().equals(""))){
            result.setText("Check your email or password");
        }
        else if(!email.getText().endsWith("@gmail.com")){
            result.setText("enter a valid email");
        }
        else{
            try {
                URL url = new URL(ManageUrl.getFristOfUrl() + "user");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                //to create JSON object with data
                JSONObject json = new JSONObject();
                json.put("firstname",fristName.getText());
                json.put("lastname",lastName.getText());
                json.put("email",email.getText());
                json.put("password",passWord.getText());
                json.put("country",country.getText());
                json.put("city",city.getText());
                json.put("additionalname",additionalname.getText());
                // to Write the JSON data to the output stream
                OutputStream os = con.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                StringBuffer response1 = new StringBuffer();
                while ((inputline = in.readLine()) != null) {
                    response1.append(inputline);
                }
                in.close();
                String response = response1.toString();
                if(response.equals("Method not allowed")){
                    result.setText("Method not allowed");
                }
                else if(response.equals("Internal server error")){
                    result.setText("Internal server error");
                }
                else if(response.equals("Bad request")){
                    result.setText("Bad request");
                }
                else if(response.equals("Invalid path")){
                    result.setText("Invalid path");
                }
                else{
                    //go to home page
                }

            } catch (Exception e) {
                result.setText("connection failed");
            }
        }
    }
}
