package Server.DAO;

import Server.models.Education;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EducationDAO {
    private final Connection connection;

    public EducationDAO(){
        connection = DataBaseConnection.getConnection();
        try {
            createEducationTable();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void createEducationTable() throws SQLException {
        PreparedStatement statement= connection.prepareStatement("CREATE TABLE IF NOT EXISTS `education` (\n" +
                "  `id` int unsigned NOT NULL AUTO_INCREMENT,\n" +
                "  `school` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,\n" +
                "  `degree` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,\n" +
                "  `fieldofstudy` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,\n" +
                "  `grade` double NOT NULL DEFAULT '0',\n" +
                "  `detail` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,\n" +
                "  `startdate` date DEFAULT NULL,\n" +
                "  `enddate` date DEFAULT NULL,\n" +
                "  `workdetail` varchar(500) DEFAULT NULL,\n" +
                "  `eduaccess` varchar(10) DEFAULT NULL,\n" +
                "  `email` varchar(40) NOT NULL,\n" +
                "  KEY `id` (`id`),\n" +
                "  KEY `email` (`email`),\n" +
                "  CONSTRAINT `FK_education_users` FOREIGN KEY (`email`) REFERENCES `users` (`email`) ON DELETE CASCADE\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
        statement.execute();
    }

    public void saveEducationDetail(Education education, String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO education (school, degree, fieldofstudy, grade, detail, startdate, enddate, workdetail, eduaccess, email) VALUES (?,?,?,?,?,?,?,?,?,?)");

        statement.setString(1, education.getSchool());
        statement.setString(2, education.getDegree());
        statement.setString(3, education.getFieldOfStudy());
        statement.setDouble(4, education.getGrade());
        statement.setString(5, education.getDetail());
        statement.setDate(6, education.getStartDate());
        statement.setDate(7, education.getEndDate());
        statement.setString(8, education.getWorkDetail());
        statement.setString(9, education.getEduAccess());
        statement.setString(10, email);

        statement.executeUpdate();
    }

    public void editEducationDetail(Education education, String email, String school) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE education SET school = ?, degree = ?, fieldofstudy = ?, grade = ?, detail = ?, startdate = ?, enddate = ?, workdetail = ?, eduaccess = ? WHERE email = ? AND school = ?");

        statement.setString(1, education.getSchool());
        statement.setString(2, education.getDegree());
        statement.setString(3, education.getFieldOfStudy());
        statement.setDouble(4, education.getGrade());
        statement.setString(5, education.getDetail());
        statement.setDate(6, education.getStartDate());
        statement.setDate(7, education.getEndDate());
        statement.setString(8, education.getWorkDetail());
        statement.setString(9, education.getEduAccess());
        statement.setString(10, email);
        statement.setString(11, school);

        statement.executeUpdate();
    }

    public ArrayList<Education> getEducations(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM education WHERE email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        Education education = new Education();
        ArrayList<Education> educations = new ArrayList<>();
        while (resultSet.next()) {
            education.setDegree(resultSet.getString("degree"));
            education.setDetail(resultSet.getString("detail"));
            education.setSchool(resultSet.getString("school"));
            education.setFieldOfStudy(resultSet.getString("fieldofstudy"));
            education.setGrade(resultSet.getDouble("grade"));
            education.setEndDate(resultSet.getDate("enddate"));
            education.setStartDate(resultSet.getDate("startdate"));
            education.setWorkDetail(resultSet.getString("workdetail"));
            education.setEduAccess(resultSet.getString("eduaccess"));
            education.setEmail(email);
            educations.add(education);
        }
        return educations;
    }

    public Education getEducation(String email, String school) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM education WHERE email = ? AND school = ?");
        statement.setString(1, email);
        statement.setString(2, school);
        ResultSet resultSet = statement.executeQuery();
        Education education = new Education();
        if (resultSet.next()) {
            education.setDegree(resultSet.getString("degree"));
            education.setDetail(resultSet.getString("detail"));
            education.setSchool(resultSet.getString("school"));
            education.setFieldOfStudy(resultSet.getString("fieldofstudy"));
            education.setGrade(resultSet.getDouble("grade"));
            education.setEndDate(resultSet.getDate("enddate"));
            education.setStartDate(resultSet.getDate("startdate"));
            education.setWorkDetail(resultSet.getString("workdetail"));
            education.setEduAccess(resultSet.getString("eduaccess"));
            education.setEmail(email);
            return education;
        } else {
            return null;
        }
    }

    public ArrayList<Education> getEducations() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM education");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Education> educations = new ArrayList<>();
        while (resultSet.next()) {
            Education education = new Education();
            education.setDegree(resultSet.getString("degree"));
            education.setDetail(resultSet.getString("detail"));
            education.setSchool(resultSet.getString("school"));
            education.setFieldOfStudy(resultSet.getString("fieldofstudy"));
            education.setGrade(resultSet.getDouble("grade"));
            education.setEndDate(resultSet.getDate("enddate"));
            education.setStartDate(resultSet.getDate("startdate"));
            education.setWorkDetail(resultSet.getString("workdetail"));
            education.setEduAccess(resultSet.getString("eduaccess"));
            education.setEmail(resultSet.getString("email"));
            educations.add(education);
        }
        return educations;
    }

    public void deleteEducation(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM education WHERE email = ?");
        statement.setString(1, email);
        statement.executeUpdate();
    }

    public void deleteEducation(String email, String school) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM education WHERE email = ? AND school = ?");

        statement.setString(1, email);
        statement.setString(2, school);

        statement.executeUpdate();
    }


    public void deleteEducations() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE * FROM education");
        statement.execute();
    }

}
