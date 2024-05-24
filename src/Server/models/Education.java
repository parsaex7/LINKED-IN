package Server.models;

import java.util.ArrayList;

public class Education {

        private String school;
        private String degree;
        private String fieldOfStudy;
        private String grade;
        private ArrayList<String> activities;
        private String detail;
        private String startDate;
        private String endDate;

        public Education(String school, String degree, String fieldOfStudy, String grade, String activities, String detail, String startDate, String endDate) {
            this.school = school;
            this.degree = degree;
            this.fieldOfStudy = fieldOfStudy;
            this.grade = grade;
            this.activities = new ArrayList<>();
            this.activities.add(activities);
            this.detail = detail;
            this.startDate = startDate;
            this.endDate = endDate;
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

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public ArrayList<String> getActivities() {
            return activities;
        }

        public void setNewActivities(String activities) {
            this.activities.add(activities);
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
}
