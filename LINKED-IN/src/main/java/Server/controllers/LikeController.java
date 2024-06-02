package Server.controllers;

import Server.DAO.LikeDAO;
import Server.models.Like;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class LikeController {
    private LikeDAO likeDAO;

    public LikeController() {
        likeDAO = new LikeDAO();
    }

    public void addLike(String email, int postId) throws SQLException {
        Like like = new Like(email, postId);
        likeDAO.saveLike(like);
    }

    public void deleteLike(String email, int postId) throws SQLException {
        Like like = new Like(email, postId);
        likeDAO.deleteLike(like);
    }

    public String getAllLikes() throws SQLException, JsonProcessingException {
        ArrayList<Like> likes = likeDAO.getAllLikes();
        if (likes == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }

    public String getAllLikesOfOnePost(int postId) throws SQLException, JsonProcessingException {
        ArrayList<Like> likes = likeDAO.getAllLikesOfOnePost(postId);
        if (likes == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }

    public String getAllLikesOfOneUser(String email) throws SQLException, JsonProcessingException {
        ArrayList<Like> likes = likeDAO.getAllLikesOfOnePerson(email);
        if (likes == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(likes);
    }

    public boolean likeExists(String email, int postId) throws SQLException {
        return likeDAO.likeExists(email, postId);
    }
}
