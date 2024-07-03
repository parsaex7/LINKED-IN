package org.example.model;



public class Like {

    private String user_email;
    private int post_id;

    public Like(String user_email, int post_id) {
        this.user_email = user_email;
        this.post_id = post_id;
    }

    public Like() {

    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}

