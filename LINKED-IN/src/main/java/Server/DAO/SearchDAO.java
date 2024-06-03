package Server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchDAO {
    private Connection connection;

    public SearchDAO() {
        connection = DataBaseConnection.getConnection();
    }

    public ArrayList<String> searchUser(String search) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT email FROM users WHERE email LIKE ? OR firstname LIKE ? OR lastname LIKE ? OR country LIKE ? OR city LIKE ? OR additionalname LIKE ?");

        statement.setString(1, "%" + search + "%");
        statement.setString(2, "%" + search + "%");
        statement.setString(3, "%" + search + "%");
        statement.setString(4, "%" + search + "%");
        statement.setString(5, "%" + search + "%");
        statement.setString(6, "%" + search + "%");

        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> emails = new ArrayList<>();
        while (resultSet.next()) {
            emails.add(resultSet.getString("email"));
        }

        return emails;
    }
}
