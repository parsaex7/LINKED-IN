package Server.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/linked-in";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "p13842005";
    private static Connection connection;

    private DataBaseConnection() throws SQLException {

    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;

    }

}