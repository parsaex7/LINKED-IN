package Server.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

public class Education {

    enum access {
        ME,
        CONTACTS,
        EVERYONE
    }

    private String school;
    private String degree;
    private String fieldOfStudy;
    private double grade;
    private ArrayList<String> activities;
    private String workDetail;
    private String detail;
    private Date startDate;
    private Date endDate;
    private access eduAccess;

    public Education(String school, String degree, String fieldOfStudy, double grade, String activities, String detail, Date startDate, Date endDate, String workDetail, String accessEdu) {
        this.school = school;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.grade = grade;
        this.activities = new ArrayList<>();
        this.activities.add(activities);
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
    }

    public Education() {

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

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setNewActivities(String activities) {
        this.activities.add(activities);
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
