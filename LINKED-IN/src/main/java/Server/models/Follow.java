package Server.models;

public class Follow {
    private String follower_email;
    private String following_email;

    public Follow(String follower_email, String following_email) {
        this.follower_email = follower_email;
        this.following_email = following_email;
    }

    public String getFollowing_email() {
        return following_email;
    }

    public void setFollowing_email(String following_email) {
        this.following_email = following_email;
    }

    public String getFollower_email() {
        return follower_email;
    }

    public void setFollower_email(String follower_email) {
        this.follower_email = follower_email;
    }
}
