package Server.models;

public class Follow {
    private String email1;//email1
    private String email2;//email
    //email1 wants to follow email2


    public Follow(String email1, String email2) {
        this.email1 = email1;
        this.email2 = email2;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }
}
