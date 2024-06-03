package Server.controllers;

import Server.DAO.FollowDAO;
import Server.Exceptions.AccessDeniedException;
import Server.Exceptions.UserNotExistException;
import Server.models.Follow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class FollowController {
    private FollowDAO followDAO;

    public FollowController() {
        followDAO = new FollowDAO();
    }

    public void createFollow(String follower_email, String following_email) throws SQLException, UserNotExistException {
        Follow newFollow = new Follow(follower_email, following_email);
        followDAO.createFollow(newFollow);
    }

    public void deleteFollow(String follower_email, String following_email, String email) throws SQLException, UserNotExistException, AccessDeniedException {
        Follow follow = new Follow(follower_email, following_email);
        followDAO.deleteFollow(follow, email);
    }

    public String getAllFollowers(String email) throws SQLException, JsonProcessingException {
        ArrayList<Follow> followers = followDAO.getAllFollowers(email);
        if (followers == null) {
            return null;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(followers);
        }
    }

    public String getAllFollowings(String email) throws SQLException, JsonProcessingException {
        ArrayList<Follow> followings = followDAO.getAllFollowings(email);
        if (followings == null) {
            return null;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(followings);
        }
    }

    public boolean isFollowExist(String follower_email, String following_email) throws SQLException {
        return followDAO.isFollowExist(follower_email, following_email);
    }
    public String getFollowers(String email) throws SQLException, JsonProcessingException {
        ArrayList<String> result=followDAO.getfollowers(email);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(result);
    }
    public String getFollowings(String email) throws SQLException, JsonProcessingException {
        ArrayList<String> result=followDAO.getfollowings(email);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(result);
    }
}
