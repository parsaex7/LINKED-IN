package Server.models;

import java.sql.Date;

public class Education {

    enum access {
        ME,
        CONTACTS,
        EVERYONE
    }

    private String email;
    private String school;
    private String degree;
    private String fieldOfStudy;
    private double grade;
    private String workDetail;
    private String detail;
    private Date startDate;
    private Date endDate;
    private access eduAccess;

    public Education(String school, String degree, String fieldOfStudy, double grade, String detail, Date startDate, Date endDate, String workDetail, String accessEdu, String email) {
        this.school = school;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.grade = grade;
        this.detail = detail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.workDetail = workDetail;
        switch (accessEdu) {
            case "me":
                this.eduAccess = access.ME;
                break;
            case "contacts":
                this.eduAccess = access.CONTACTS;
                break;
            case "everyone":
                this.eduAccess = access.EVERYONE;
                break;
        }
        this.email = email;
    }

    public Education() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getWorkDetail() {
        return workDetail;
    }

    public void setWorkDetail(String workDetail) {
        this.workDetail = workDetail;
    }

    public String getEduAccess() {
        return eduAccess.name().toLowerCase();
    }

    public void setEduAccess(String accessEdu) {
        switch (accessEdu) {
            case "me":
                this.eduAccess = access.ME;
                break;
            case "contacts":
                this.eduAccess = access.CONTACTS;
                break;
            case "everyone":
                this.eduAccess = access.EVERYONE;
                break;
        }
    }
}
