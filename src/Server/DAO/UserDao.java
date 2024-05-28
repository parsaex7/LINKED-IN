package Server.DAO;

import Server.models.User;

import java.sql.*;

public class UserDao {
    private final Connection connection;
    public UserDao(){
        connection=DataBaseConnection.getConnection();
    }
    public void saveUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (firstname,lastname,email,password,country,city,additionalname,birthdate,registrationDate) VALUES (?,?,?,?,?,?,?,?,?)");
        statement.setString(1,user.getName());
        statement.setString(2, user.getLastName());
        statement.setString(3,user.getEmail());
        statement.setString(4, user.getPassword());
        statement.setString(5, user.getCountry());
        statement.setString(6,user.getCity());
        statement.setString(7,user.getAdditionalName());
        statement.setDate(8,user.getBirthDate());
        statement.setDate(9,user.getRegistrationDate());
        statement.executeUpdate();
        Statement statement1 = connection.createStatement();
        String query = "select id from users where email = " + "'" + user.getEmail() + "'";
        ResultSet resultSet = statement1.executeQuery(query);
        user.setId(resultSet.getInt(1));
    }
    public void deleteUser(User user)throws SQLException{
        PreparedStatement statement=connection.prepareStatement("DELETE FROM users WHERE id=?");
        statement.setInt(1,user.getId());
        statement.executeUpdate();

    }
    public void deleteUsers() throws SQLException{
        PreparedStatement statement=connection.prepareStatement("DELETE FROM users ");
        statement.executeUpdate();

    }
    public void upDateUser(User user) throws SQLException{
        PreparedStatement statement=connection.prepareStatement("UPDATE users SET fristname=?,lastname=?,email=?,password=?,country=?,city=?,additionalname=?,birthdate=?,registrationDate=? WHERE id=?");
        statement.setString(1,user.getName());
        statement.setString(2, user.getLastName());
        statement.setString(3,user.getEmail());
        statement.setString(4, user.getPassword());
        statement.setString(5, user.getCountry());
        statement.setString(6, user.getCity());
        statement.setString(7, user.getAdditionalName());
        statement.setDate(8,user.getBirthDate());
        statement.setDate(9,user.getRegistrationDate());
        statement.setInt(10,user.getId());
        statement.executeUpdate();
    }


}
