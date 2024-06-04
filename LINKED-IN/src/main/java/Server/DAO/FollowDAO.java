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


    public void follow(Follow follow) throws SQLException, UserNotExistException, AlreadyFollowed {//following_email wants to follow follower_email
        String following_email = follow.getFollowing_email();
        String follower_email = follow.getFollower_email();
        if ((userDao.getUser(following_email) == null) || (userDao.getUser(follower_email) == null)) {
            throw new UserNotExistException();
        }
        if (isFollowExist(follower_email, following_email)) {
            throw new AlreadyFollowed();
        }
        PreparedStatement statement = connection.prepareStatement("INSERT INTO follows (following_email, follower_email) VALUES (?,?)");
        statement.setString(1, following_email);
        statement.setString(2, follower_email);
        statement.executeUpdate();
    }

    public void unFollow(Follow follow) throws SQLException, UserNotExistException, followingNotFound {//following_email wants to unfollow follower_email
        String following_email = follow.getFollowing_email();
        String follower_email = follow.getFollower_email();
        if ((userDao.getUser(following_email) == null) || (userDao.getUser(follower_email) == null)) {
            throw new UserNotExistException();
        }
        PreparedStatement checkStatement = connection.prepareStatement("SELECT  * FROM follows WHERE following_email = ? AND follower_email = ?");
        checkStatement.setString(1, following_email);
        checkStatement.setString(2, follower_email);
        ResultSet resultSet = checkStatement.executeQuery();
        if (!resultSet.next()) {
            throw new followingNotFound();
        }
        PreparedStatement statement = connection.prepareStatement("DELETE FROM follows WHERE following_email = ? AND follower_email = ?");
        statement.setString(1, following_email);
        statement.setString(2, follower_email);
        statement.executeUpdate();
    }


    public boolean isFollowExist(String follower_email, String following_email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM follows WHERE follower_email = ? AND following_email = ?");
        statement.setString(1, follower_email);
        statement.setString(2, following_email);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public ArrayList<String> getFollowers(String email) throws SQLException, followingNotFound, UserNotExistException {
        if (!userDao.isUserExist(email)) {
            throw new UserNotExistException();
        }
        ArrayList<String> followers = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT following_email FROM follows WHERE follower_email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        String following_email;
        while (resultSet.next()) {
            following_email = resultSet.getString("following_email");
            followers.add(following_email);
        }
        if (followers.isEmpty()) {
            throw new followingNotFound();
        } else {
            return followers;
        }
    }

    public ArrayList<String> getFollowings(String email) throws SQLException, followingNotFound, UserNotExistException {
        if (!userDao.isUserExist(email)) {
            throw new UserNotExistException();
        }
        ArrayList<String> followings = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT follower_email FROM follows WHERE following_email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        String follower_email;
        while (resultSet.next()) {
            follower_email = resultSet.getString("follower_email");
            followings.add(follower_email);
        }
        if (followings.isEmpty()) {
            throw new followingNotFound();
        } else {
            return followings;
        }
    }

}
