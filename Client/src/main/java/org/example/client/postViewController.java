package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.ByteBuffer;

public class postViewController {

    @FXML
    private TextArea textArea;
    @FXML
    private Label result;

    private static String file_path;

    public void onBack(ActionEvent event) {
        try {
            file_path = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onPost() {
        try {
            if (textArea.getText().isEmpty()) {
                result.setText("Please write something");
                return;
            }
            URL url = new URL(Functions.getFirstOfUrl() + "post/" + file_path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("JWT", LinkedInApplication.user.getToken());
            connection.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message", textArea.getText());
            Functions.sendResponse(connection, jsonObject.toString());
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                result.setText("Posted Successfully");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
                Stage stage = (Stage) textArea.getScene().getWindow();
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Functions.fadeScene(stage, scene);
            } else {
                result.setText("Internal Error");
            }
        }catch (Exception e){
            result.setText("Internal Error");
        }
    }
    public void onSetting(ActionEvent event){

    }

    public void searchButtonController(ActionEvent event) {
        try {
            file_path = null;
            otherProfileView.isAuth = true;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchUser-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            Functions.fadeScene(stage, scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void profileController(ActionEvent event) throws IOException {
        file_path = null;
        otherProfileView.isAuth = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }

    public void fileController(ActionEvent event) throws MalformedURLException {
        String path;
        Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
            path = String.valueOf(file.toURL());
        else
            return;
        file_path = file.getName().trim();
        System.out.println(file_path);
        saveFile(new Image(path), file.getName().trim());
    }

    public void homeController(ActionEvent event) throws IOException {
        file_path = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) result.getScene().getWindow();
        Scene scene = new Scene(root);
        Functions.fadeScene(stage, scene);
    }

    private void saveFile(Image image, String name) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        // Get PixelReader
        PixelReader pixelReader = image.getPixelReader();

        // Create a BufferedImage
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Read pixel data into BufferedImage
        WritablePixelFormat<ByteBuffer> format = WritablePixelFormat.getByteBgraInstance();
        byte[] buffer = new byte[width * height * 4]; // 4 bytes per pixel (RGBA)
        pixelReader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);
        // Set pixel data to BufferedImage
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = (y * width + x) * 4;
                int argb = ((buffer[i + 3] & 0xFF) << 24) | // Alpha
                        ((buffer[i + 2] & 0xFF) << 16) | // Red
                        ((buffer[i + 1] & 0xFF) << 8)  | // Green
                        (buffer[i] & 0xFF);             // Blue
                bufferedImage.setRGB(x, y, argb);
            }
        }

        // Save to file
        File outputFile = new File("src\\main\\resources\\org\\example\\post\\" + name);
        try {
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Image saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
    }
}
