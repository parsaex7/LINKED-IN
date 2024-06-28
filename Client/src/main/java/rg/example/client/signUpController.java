package rg.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

            } catch (Exception e) {
                result.setText("connection failed");
            }
        }
    }
}
