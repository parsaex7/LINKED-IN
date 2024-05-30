package Server.DAO;

import Server.models.Education;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EducationDAO {
    private final Connection connection;

    public EducationDAO() {
        connection = DataBaseConnection.getConnection();
    }

    public void saveEducationDetail(Education education, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO education VALUES (?,?,?,?,?,?,?,?,?,?)");

        statement.setInt(1, id);
        statement.setString(2, education.getSchool());
        statement.setString(3, education.getDegree());
        statement.setString(4, education.getFieldOfStudy());
        statement.setDouble(5, education.getGrade());
        statement.setString(6, education.getDetail());
        statement.setDate(7, education.getStartDate());
        statement.setDate(8, education.getEndDate());
        statement.setString(9, education.getWorkDetail());
        statement.setString(10, education.getEduAccess());

        statement.executeUpdate();
    }

   public void editEducationDetail(Education education, int id, String school) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("UPDATE education SET school = ?, degree = ?, fieldofstudy = ?, grade = ?, detail = ?, startdate = ?, enddate = ?, workdetail = ?, eduaccess = ? WHERE id = ? AND school = ?");

    statement.setString(1, education.getSchool());
    statement.setString(2, education.getDegree());
    statement.setString(3, education.getFieldOfStudy());
    statement.setDouble(4, education.getGrade());
    statement.setString(5, education.getDetail());
    statement.setDate(6, education.getStartDate());
    statement.setDate(7, education.getEndDate());
    statement.setString(8, education.getWorkDetail());
    statement.setString(9, education.getEduAccess());
    statement.setInt(10, id);
    statement.setString(11, school);

    statement.executeUpdate();
}

    //return all of a user educations
    public ArrayList<Education> getAllEducationDetail(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM education WHERE id = ?");

        statement.setInt(1, id);

        ResultSet resultset = statement.executeQuery();
        ArrayList<Education> educations = new ArrayList<>();
        while (resultset.next()) {
            Education education = new Education();
            education.setSchool(resultset.getString("school"));
            education.setDegree(resultset.getString("degree"));
            education.setFieldOfStudy(resultset.getString("fieldofstudy"));
            education.setGrade(resultset.getDouble("grade"));
            education.setDetail(resultset.getString("detail"));
            education.setStartDate(resultset.getDate("startdate"));
            education.setEndDate(resultset.getDate("enddate"));
            education.setWorkDetail(resultset.getString("workdetail"));
            education.setEduAccess(resultset.getString("eduaccess"));

            educations.add(education);
        }

        return educations;
    }
    public void deleteEducation(int id) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM education where =?");
        statement.setInt(1,id);
        statement.executeUpdate();
    }
    public Education getEducation(int id) throws SQLException {
        PreparedStatement statement=connection.prepareStatement("SELECT * from education WHERE id=?");
        statement.setInt(1,id);
        ResultSet resultSet=statement.executeQuery();
        Education toReturn=new Education();
        if(resultSet.next()){
            toReturn.setDegree(resultSet.getString("degree"));
            toReturn.setDetail(resultSet.getString("detail"));
            toReturn.setSchool(resultSet.getString("school"));
            toReturn.setFieldOfStudy(resultSet.getString("fieldofstudy"));
            toReturn.setGrade(resultSet.getDouble("grade"));
            toReturn.setEndDate(resultSet.getDate("enddate"));
            toReturn.setStartDate(resultSet.getDate("enddate"));
        }
        return toReturn;
    }
    public void deleteAllEducation() throws SQLException {
        PreparedStatement statement=connection.prepareStatement("DELETE FROM education");
        statement.execute();
    }
        //id-school-degree-fieldostudy-grade-detail-startdae-enddate-workdetail-eduaccess
}
