package Server.DAO;

import Server.Exceptions.UserNotExistException;
import Server.models.Follow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FollowDAO {
    private final Connection connection;
    UserDao userDao=new UserDao();

    public FollowDAO() {
        this.connection = DataBaseConnection.getConnection();
    }

    public void follow(Follow follow) throws SQLException, UserNotExistException {//email1 wants to follow email2
        String email1= follow.getEmail1();
        String email2= follow.getEmail2();
        if((userDao.getUser(email1)==null)||(userDao.getUser(email2)==null)){
            throw new UserNotExistException();
        }
        PreparedStatement statement= connection.prepareStatement("INSERT INTO follow(email1,email2) VALUES (?,?)");
        statement.setString(1,email1);
        statement.setString(2,email2);
    }
    public void unFollow(Follow follow) throws SQLException, UserNotExistException {//email1 wants to unFollow email2
        String email1= follow.getEmail1();
        String email2= follow.getEmail2();
        if((userDao.getUser(email1)==null)||(userDao.getUser(email2)==null)){
            throw new UserNotExistException();
        }
    PreparedStatement statement= connection.prepareStatement("DELETE FROM follow WHERE emial1=? AND email2=?");
        statement.setString(1,email1);
        statement.setString(2,email2);
    }
}
