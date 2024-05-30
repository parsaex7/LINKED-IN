package Server.controllers;

import Server.DAO.EducationDAO;
import Server.models.Education;

import java.sql.Date;
import java.sql.SQLException;

public class EducationController {
    static int id=1;
    private final EducationDAO  educationDAO;
    public EducationController(){
        educationDAO=new EducationDAO();
    }
    public void createEducation(String school, String degree, String fieldOfStudy, double grade, String activities, String detail, Date startDate, Date endDate, String workDetail, String accessEdu) throws SQLException {
        Education toAdd=new Education(school,degree,fieldOfStudy,grade,activities,detail,startDate,endDate,workDetail,accessEdu);
        educationDAO.saveEducationDetail(toAdd,id);
        id++;
    }
    public void updateEducation(String school, String degree, String fieldOfStudy, double grade, String activities, String detail, Date startDate, Date endDate, String workDetail, String accessEdu,int id) throws SQLException {
        Education  toUpdate=new Education(school,degree,fieldOfStudy,grade,activities,detail,startDate,endDate,workDetail,accessEdu);
        educationDAO.editEducationDetail(toUpdate,id,school);
    }
    public void deleteEducation(int id) throws SQLException {
        educationDAO.deleteEducation(id);
    }
    public Education gerEducationById(int id) throws SQLException {
            return educationDAO.getEducation(id);
    }
    public void deleteAllEducation() throws SQLException {
        educationDAO.deleteAllEducation();
    }
}
