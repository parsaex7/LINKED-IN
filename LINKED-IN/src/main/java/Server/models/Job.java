package Server.models;

import java.util.ArrayList;

public class Job {
    private String jobTitle;
    private String company;
    private String jobType;
    private String startDate;
    private String endDate;
    private ArrayList<String> skills;
    private String description;
    private boolean active; // true if the user is currently working in this job

    public Job(String jobTitle, String company, String jobType, String startDate, String skills, String description) {
        this.jobTitle = jobTitle;
        this.company = company;
        this.jobType = jobType;
        this.startDate = startDate;
        this.endDate = null;
        this.skills = new ArrayList<>();
        this.skills.add(skills);
        this.description = description;
        this.active = true;
    } //if job is active

    public Job(String jobTitle, String company, String jobType, String startDate, String endDate, String skills, String description) {
        this.jobTitle = jobTitle;
        this.company = company;
        this.jobType = jobType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.skills = new ArrayList<>();
        this.skills.add(skills);
        this.description = description;
        this.active = false;
    } //if  job is not active

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(String skill) {
        skills.add(skill);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
