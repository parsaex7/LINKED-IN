package Server.DAO;

import Server.models.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDao {
    private final Connection connection;
    public UserDao(){
        connection=DataBaseConnection.getConnection();
    }
    public void saveUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (fristname,lastname,email,password,country,city,additionalname,birthdate,registrationDate) VALUES (?,?,?,?,?,?,?,?,?)");
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
    public User getUser(int id) throws SQLException{
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM users WHERE id=?");
        statement.setInt(1,id);
        ResultSet resultSet=statement.executeQuery();
        User toGet=new User();
        if(resultSet.next()){
            toGet.setId(id);
            toGet.setAdditionalName(resultSet.getString("additionalname"));
            toGet.setCity(resultSet.getString("city"));
            toGet.setEmail(resultSet.getString("email"));
            toGet.setCountry(resultSet.getString("country"));
            toGet.setBirthDate(resultSet.getDate("birthdate"));
            toGet.setName(resultSet.getString("fristname"));
            toGet.setLastName(resultSet.getString("lastname"));
            toGet.setBirthDate(resultSet.getDate("birthdate"));
            toGet.setRegistrationDate(resultSet.getDate("registrationDate"));
            toGet.setPassword(resultSet.getString("password"));
        }
        return toGet;
    }
    public User getUser (String name,String lastname,String password) throws SQLException{
        PreparedStatement statement=connection.prepareStatement("SELECT * FROM users WHERE name=? AND lastname=? AND password=?");
        statement.setString(1,name);
        statement.setString(2,lastname);
        statement.setString(3,password);
        ResultSet resultSet=statement.executeQuery();
        User toGet=new User();
        if(resultSet.next()){
            toGet.setAdditionalName(resultSet.getString("additionalname"));
            toGet.setCity(resultSet.getString("city"));
            toGet.setEmail(resultSet.getString("email"));
            toGet.setCountry(resultSet.getString("country"));
            toGet.setBirthDate(resultSet.getDate("birthdate"));
            toGet.setName(resultSet.getString("fristname"));
            toGet.setLastName(resultSet.getString("lastname"));
            toGet.setBirthDate(resultSet.getDate("birthdate"));
            toGet.setRegistrationDate(resultSet.getDate("registrationDate"));
            toGet.setPassword(resultSet.getString("password"));
        }
        return toGet;
    }
    public ArrayList<User> getUssers() throws SQLException{
        PreparedStatement statement=connection.prepareStatement("SLECT * FROM users");
        ResultSet resultSet=statement.executeQuery();
        ArrayList<User> toReturn=new ArrayList<>();
        while(resultSet.next()){
            User toAdd=new User();
            toAdd.setId(resultSet.getInt("id"));
            toAdd.setAdditionalName(resultSet.getString("additionalname"));
            toAdd.setCity(resultSet.getString("city"));
            toAdd.setEmail(resultSet.getString("email"));
            toAdd.setCountry(resultSet.getString("country"));
            toAdd.setBirthDate(resultSet.getDate("birthdate"));
            toAdd.setName(resultSet.getString("fristname"));
            toAdd.setLastName(resultSet.getString("lastname"));
            toAdd.setBirthDate(resultSet.getDate("birthdate"));
            toAdd.setRegistrationDate(resultSet.getDate("registrationDate"));
            toAdd.setPassword(resultSet.getString("password"));
            toReturn.add(toAdd);

        }
        return  toReturn;
    }
}