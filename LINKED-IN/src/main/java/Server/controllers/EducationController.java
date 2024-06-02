package Server.controllers;

import Server.DAO.EducationDAO;
import Server.models.Education;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class EducationController {
    private final EducationDAO educationDAO;

    public EducationController() {
        educationDAO = new EducationDAO();
    }

    public void createEducation(String school, String degree, String fieldOfStudy, double grade, String activities, String detail, Date startDate, Date endDate, String workDetail, String accessEdu, String email) throws SQLException {
        Education education = new Education(school, degree, fieldOfStudy, grade, activities, detail, startDate, endDate, workDetail, accessEdu, email);
        educationDAO.saveEducationDetail(education, email);
    }

    public void updateEducation(String school, String degree, String fieldOfStudy, double grade, String activities, String detail, Date startDate, Date endDate, String workDetail, String accessEdu, String email) throws SQLException {
        Education education = new Education(school, degree, fieldOfStudy, grade, activities, detail, startDate, endDate, workDetail, accessEdu, email);
        educationDAO.editEducationDetail(education, email, school);
    }

    public void deleteEducationByEmail(String email) throws SQLException {
        educationDAO.deleteEducation(email);
    }

    public void deleteEducationByEmailAndSchool(String email, String school) throws SQLException {
        educationDAO.deleteEducation(email, school);
    }

    public String getEducations(String email) throws SQLException, JsonProcessingException {
        ArrayList<Education> educations= educationDAO.getEducations(email);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(educations);
    }

    public Education getEducationByEmailAndSchool(String email, String school) throws SQLException {
        return educationDAO.getEducation(email, school);
    }

    public void deleteAllEducation() throws SQLException {
        educationDAO.deleteEducations();
    }

    public String  getAllEducations() throws SQLException, JsonProcessingException {
        ArrayList<Education> educations= educationDAO.getEducations();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(educations);
    }
}
