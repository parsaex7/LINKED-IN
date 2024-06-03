package Server.DAO;

import Server.models.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDao {

    private final Connection connection;

    public UserDao() {
        connection = DataBaseConnection.getConnection();
    }


    public void saveUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (firstname, lastname, email, password, country, city, additionalname, birthdate, registrationDate) VALUES (?,?,?,?,?,?,?,?,?)");
        statement.setString(1, user.getName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getEmail().toLowerCase());
        statement.setString(4, user.getPassword());
        statement.setString(5, user.getCountry());
        statement.setString(6, user.getCity());
        statement.setString(7, user.getAdditionalName());
        statement.setDate(8, user.getBirthDate());
        statement.setDate(9, user.getRegistrationDate());
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("duplicate email");
        }
    }

    public void deleteUser(User user) throws SQLException {
        if (user == null) {
            System.out.println("not-found");
            return;
        }
        PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE email = ?");
        statement.setString(1, user.getEmail());
        statement.executeUpdate();
    }


    public void deleteUsers() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM users");
        statement.execute();
    }

    public void updateUser(User user, String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET firstname = ?, lastname = ?, email = ?, password = ?, country = ?, city = ?, additionalname = ?, birthdate = ?, registrationDate = ? WHERE email = ?");
        statement.setString(1, user.getName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getEmail().toLowerCase());
        statement.setString(4, user.getPassword());
        statement.setString(5, user.getCountry());
        statement.setString(6, user.getCity());
        statement.setString(7, user.getAdditionalName());
        statement.setDate(8, user.getBirthDate());
        statement.setDate(9, user.getRegistrationDate());
        statement.setString(10, email);
        statement.executeUpdate();
    }


    public User getUser(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        User user = new User();
        if (resultSet.next()) {
            user.setAdditionalName(resultSet.getString("additionalname"));
            user.setCity(resultSet.getString("city"));
            user.setEmail(resultSet.getString("email"));
            user.setCountry(resultSet.getString("country"));
            user.setBirthDate(resultSet.getDate("birthdate"));
            user.setName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setBirthDate(resultSet.getDate("birthdate"));
            user.setRegistrationDate(resultSet.getDate("registrationdate"));
            user.setPassword(resultSet.getString("password"));
        } else {
            return null;
        }
        return user;
    }

    public User getUser(String email, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        User user = new User();
        if (resultSet.next()) {
            user.setAdditionalName(resultSet.getString("additionalname"));
            user.setCity(resultSet.getString("city"));
            user.setEmail(resultSet.getString("email"));
            user.setCountry(resultSet.getString("country"));
            user.setBirthDate(resultSet.getDate("birthdate"));
            user.setName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setBirthDate(resultSet.getDate("birthdate"));
            user.setRegistrationDate(resultSet.getDate("registrationdate"));
            user.setPassword(resultSet.getString("password"));
        } else {
            return null;
        }
        return user;
    }

    public ArrayList<User> getUsers() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setAdditionalName(resultSet.getString("additionalname"));
            user.setCity(resultSet.getString("city"));
            user.setEmail(resultSet.getString("email"));
            user.setCountry(resultSet.getString("country"));
            user.setBirthDate(resultSet.getDate("birthdate"));
            user.setName(resultSet.getString("firstname"));
            user.setLastName(resultSet.getString("lastname"));
            user.setBirthDate(resultSet.getDate("birthdate"));
            user.setRegistrationDate(resultSet.getDate("registrationdate"));
            user.setPassword(resultSet.getString("password"));
            users.add(user);
        }
        return users;
    }

    public boolean isUserExist(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}