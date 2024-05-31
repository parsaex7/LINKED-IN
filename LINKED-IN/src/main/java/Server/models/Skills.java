package Server.models;

public class Skills {
    private String Email;
    private String skill1;
    private String skill2;
    private String skill3;
    private String skill4;
    private String skill5;

    public Skills(String email) {
        Email = email;
    }

    public Skills(String email, String skill1) {
        Email = email;
        this.skill1 = skill1;
    }

    public Skills(String email, String skill1, String skill2) {
        Email = email;
        this.skill1 = skill1;
        this.skill2 = skill2;
    }

    public Skills(String email, String skill1, String skill2, String skill3) {
        Email = email;
        this.skill1 = skill1;
        this.skill2 = skill2;
        this.skill3 = skill3;
    }

    public Skills(String email, String skill1, String skill2, String skill3, String skill4) {
        Email = email;
        this.skill1 = skill1;
        this.skill2 = skill2;
        this.skill3 = skill3;
        this.skill4 = skill4;
    }

    public Skills(String email, String skill1, String skill2, String skill3, String skill4, String skill5) {
        Email = email;
        this.skill1 = skill1;
        this.skill2 = skill2;
        this.skill3 = skill3;
        this.skill4 = skill4;
        this.skill5 = skill5;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSkill1() {
        return skill1;
    }

    public void setSkill1(String skill1) {
        this.skill1 = skill1;
    }

    public String getSkill2() {
        return skill2;
    }

    public void setSkill2(String skill2) {
        this.skill2 = skill2;
    }

    public String getSkill3() {
        return skill3;
    }

    public void setSkill3(String skill3) {
        this.skill3 = skill3;
    }

    public String getSkill4() {
        return skill4;
    }

    public void setSkill4(String skill4) {
        this.skill4 = skill4;
    }

    public String getSkill5() {
        return skill5;
    }

    public void setSkill5(String skill5) {
        this.skill5 = skill5;
    }
}


