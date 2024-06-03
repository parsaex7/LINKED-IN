package Server.DAO;

import Server.Exceptions.AccessDeniedException;
import Server.Exceptions.UserNotExistException;
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

    public void createFollow(Follow follow) throws SQLException, UserNotExistException {
        if (userDao.isUserExist(follow.getFollower_email()) && userDao.isUserExist(follow.getFollowing_email())) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO follows (follower_email, following_email) VALUES (?,?)");
            statement.setString(1, follow.getFollower_email());
            statement.setString(2, follow.getFollowing_email());
            statement.executeUpdate();
        } else {
            throw new UserNotExistException();
        }
    }

    public void deleteFollow(Follow follow, String email) throws SQLException, AccessDeniedException, UserNotExistException {
        if (userDao.isUserExist(follow.getFollower_email()) && userDao.isUserExist(follow.getFollowing_email())) {
            if (email.equals(follow.getFollowing_email()) || email.equals(follow.getFollower_email())) {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM follows WHERE follower_email = ? AND following_email = ?");
                statement.setString(1, follow.getFollower_email());
                statement.setString(2, follow.getFollowing_email());
                statement.executeUpdate();
            } else {
                throw new AccessDeniedException();
            }
        } else throw new UserNotExistException();
    }

    public ArrayList<Follow> getAllFollowers(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE follower_email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Follow> followers = new ArrayList<>();
        while (resultSet.next()) {
            Follow follow = new Follow(resultSet.getString("follower_email"), resultSet.getString("following_email"));
            followers.add(follow);
        }
        if (followers.isEmpty()) {
            return null;
        } else {
            return followers;
        }
    }

    public ArrayList<Follow> getAllFollowings(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE following_email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Follow> followings = new ArrayList<>();
        while (resultSet.next()) {
            Follow follow = new Follow(resultSet.getString("follower_email"), resultSet.getString("following_email"));
            followings.add(follow);
        }
        if (followings.isEmpty()) {
            return null;
        } else {
            return followings;
        }
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
