package org.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class logInViewController {
    @FXML
    private TextField email;
    @FXML
    private TextField passWord;
    @FXML
    private Button done;
    @FXML
    private Label result;
    public void onDone()  {
        try {
            if ((email.getText() .equals("") ) || (passWord.getText() .equals(""))) {
                result.setText("Please compelte fields");
            } else if (!email.getText().endsWith("@gmail.com")) {
                result.setText("please check your email");
            } else {
                URL url = new URL(ManageUrl.getFristOfUrl()+"login/" + email.getText() + "/" + passWord.getText());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                StringBuffer response1 = new StringBuffer();
                while ((inputline = in.readLine()) != null) {
                    response1.append(inputline);
                }
                in.close();
                String response = response1.toString();
                if (response.equals("User not found OR invalid password")) {
                    result.setText("User not found OR invalid password");
                } else if (response.equals("Error in login user")) {
                    result.setText("Error in login user");
                } else if (response.equals("Invalid request")) {
                    result.setText("Invalid request");
                } else if (response.equals("Method not allowed")) {
                    result.setText("Method not allowed");
                } else if (response.startsWith("Welcome")) {
                    //new page
                }
            }
        }catch (Exception e){
            result.setText("connection failed");
        }
    }
}
