package Server.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Post {

    private int postId;
    private String message;
    private String senderEmail;

    public Post(String message, String senderEmail) {
        this.message = message;
        this.senderEmail = senderEmail;
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
}
