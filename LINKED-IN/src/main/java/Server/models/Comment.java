package Server.models;

public class Comment {

    private String user_email;
    private String comment;
    private int post_id;
    private int comment_id;

    public Comment(String user_email, String comment, int post_id, int comment_id) {
        this.user_email = user_email;
        this.comment = comment;
        this.post_id = post_id;
        this.comment_id = comment_id;
    }

    public Comment(String user_email, String comment, int post_id) {
        this.user_email = user_email;
        this.comment = comment;
        this.post_id = post_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }
}
