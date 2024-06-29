package org.example.client;

import org.example.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.regex.Pattern;

public class Functions {
    public static boolean patternMatches(String emailAddress) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static String getFirstOfUrl(){
        return "http://localhost:8000/";
    }

    public static String getResponse(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputline;
        StringBuilder response = new StringBuilder();
        while ((inputline = in.readLine()) != null) {
            response.append(inputline);
        }
        in.close();
        return response.toString();
    }

    public static void sendResponse(HttpURLConnection connection, String response) throws IOException {
        connection.getOutputStream().write(response.getBytes());
        connection.getOutputStream().close();
    }

    public static void saveUser(String email, String password, String firstName, String lastName, String token) {
        LinkedInApplication.user = new User(email, password, token, firstName, lastName);
    }
}
