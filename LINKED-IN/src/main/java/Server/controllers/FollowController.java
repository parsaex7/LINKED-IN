package Server.controllers;

import Server.DAO.FollowDAO;
import Server.Exceptions.AlreadyFollowedException;
import Server.Exceptions.AlreadyFollowedException;
import Server.Exceptions.UserNotExistException;
import Server.Exceptions.followingNotFound;
import Server.Exceptions.followingNotFound;
import Server.models.Follow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class FollowController {
    private final FollowDAO followDAO = new FollowDAO();

    public void follow(String following_email, String follower_email) throws SQLException, UserNotExistException, AlreadyFollowedException {
        //following_email wants to follow follower_email
        Follow follow = new Follow(following_email, follower_email);
        followDAO.follow(follow);
    }

    public void unFollow(String following_email, String follower_email) throws SQLException, UserNotExistException, followingNotFound {
        //following_email wants to unfollow follower_email
        Follow follow = new Follow(following_email, follower_email);
        followDAO.unFollow(follow);
    }

    public String getFollowers(String email) throws SQLException, JsonProcessingException, UserNotExistException, followingNotFound {
        ArrayList<String> followers = followDAO.getFollowers(email);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(followers);
    }

    public String getFollowings(String email) throws SQLException, JsonProcessingException, followingNotFound, UserNotExistException {
        ArrayList<String> followings = followDAO.getFollowings(email);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(followings);
    }
}
