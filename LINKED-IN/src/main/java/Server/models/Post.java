package Server.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Post {

    private int postId;
    private String message;
    private String senderEmail;
    private String file_path;

    public Post(String message, String senderEmail) {
        this.message = message;
        this.senderEmail = senderEmail;
    }

    public Post(int postId, String message, String senderEmail) {
        this.postId = postId;
        this.message = message;
        this.senderEmail = senderEmail;
    }
    public Post(int postId, String message, String senderEmail, String file_path) {
        this.postId = postId;
        this.message = message;
        this.senderEmail = senderEmail;
        this.file_path = file_path;
    }
    public Post(String message, String senderEmail, String file_path) {
        this.postId = postId;
        this.message = message;
        this.senderEmail = senderEmail;
        this.file_path = file_path;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
