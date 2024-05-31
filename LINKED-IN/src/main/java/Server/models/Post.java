package Server.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Post {
    private String messasge;
    private String senderEmail;
    private ArrayList<String> likeEmails;
    private HashMap<String ,String> coments;//frist String is email second String is message of comment

    public Post(String messasge, String senderEmail) {
        this.messasge = messasge;
        this.senderEmail = senderEmail;
        likeEmails=new ArrayList<>();
        coments=new HashMap<>();
    }

    public String getMessasge() {
        return messasge;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public ArrayList<String> getLikeEmails() {
        return likeEmails;
    }

    public HashMap<String, String> getComents() {
        return coments;
    }

    public void setMessasge(String messasge) {
        this.messasge = messasge;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setLikeEmails(ArrayList<String> likeEmail) {
        this.likeEmails = likeEmail;
    }

    public void setComents(HashMap<String, String> coments) {
        this.coments = coments;
    }
}
