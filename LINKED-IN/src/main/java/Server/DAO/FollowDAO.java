package Server.DAO;


import Server.Exceptions.AlreadyFollowed;
import Server.Exceptions.UserNotExistException;
import Server.Exceptions.followingNotFound;
import Server.models.Follow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FollowDAO {
    private final Connection connection;
    private UserDao userDao = new UserDao();

    public FollowDAO() {
        this.connection = DataBaseConnection.getConnection();
    }


    public void follow(Follow follow) throws SQLException, UserNotExistException, AlreadyFollowed {//email1 wants to follow email2
        String email1= follow.getEmail1();
        String email2= follow.getEmail2();
        if((userDao.getUser(email1)==null)||(userDao.getUser(email2)==null)){
            throw new UserNotExistException();
        }
        PreparedStatement checkStatement= connection.prepareStatement("SELECT  * FROM follow WHERE email1=? And email2=? ");
        checkStatement.setString(1,email1);
        checkStatement.setString(2,email2);
        ResultSet resultSet=checkStatement.executeQuery();
        if(resultSet==null){
            throw new AlreadyFollowed();
        }
        PreparedStatement statement= connection.prepareStatement("INSERT INTO follow(email1,email2) VALUES (?,?)");
        statement.setString(1,email1);
        statement.setString(2,email2);
        statement.executeUpdate();
    }
    public void unFollow(Follow follow) throws SQLException, UserNotExistException, followingNotFound {//email1 wants to unFollow email2
        String email1= follow.getEmail1();
        String email2= follow.getEmail2();
        if((userDao.getUser(email1)==null)||(userDao.getUser(email2)==null)){
            throw new UserNotExistException();
        }
        PreparedStatement checkStatement= connection.prepareStatement("SELECT  * FROM follow WHERE email1=? And email2=? ");
        checkStatement.setString(1,email1);
        checkStatement.setString(2,email2);
        ResultSet resultSet=checkStatement.executeQuery();
        if(resultSet==null){
            throw new followingNotFound();
        }
        PreparedStatement statement= connection.prepareStatement("DELETE FROM follow WHERE emial1=? AND email2=?");
        statement.setString(1,email1);
        statement.setString(2,email2);
        statement.executeUpdate();
    }


    public boolean isFollowExist(String follower_email, String following_email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE follower_email = ? AND following_email = ?");
        statement.setString(1, follower_email);
        statement.setString(2, following_email);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public ArrayList<String> getfollowers(String email) throws SQLException {
        ArrayList<String> result=new ArrayList<>();
        PreparedStatement statement= connection.prepareStatement("SELECT * FROM follow WHERE email2=?");
        statement.setString(1,email);
        ResultSet resultSet=statement.executeQuery();
        String emailToAdd;
        while (resultSet.next()){
            emailToAdd=resultSet.getString("email1");
            result.add(emailToAdd);
        }
        return result;
    }
    public ArrayList<String> getfollowings(String email) throws SQLException {
        ArrayList<String> result=new ArrayList<>();
        PreparedStatement statement= connection.prepareStatement("SELECT * FROM follow WHERE email2=?");
        statement.setString(1,email);
        ResultSet resultSet=statement.executeQuery();
        String emailToAdd;
        while (resultSet.next()){
            emailToAdd=resultSet.getString("email2");
            result.add(emailToAdd);
        }
        return result;
    }

}
