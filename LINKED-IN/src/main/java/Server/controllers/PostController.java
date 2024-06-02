package Server.controllers;

import Server.DAO.PostDAO;
import Server.models.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class PostController {
    private PostDAO postDAO;

    public PostController() {
        postDAO = new PostDAO();
    }

    public void addPost(String message, String senderEmail) throws SQLException {
        Post post = new Post(message, senderEmail);
        postDAO.savePost(post);
    }

    public void updatePost(int postId, String message, String senderEmail) throws SQLException {
        Post post = new Post(postId, message, senderEmail);
        postDAO.updatePost(post);
    }

    public void deletePost(int postId, String email) throws SQLException {
        Post post = new Post(postId, "", email);
        postDAO.deletePost(post);
    }

    public void deleteAllPosts() throws SQLException {
        postDAO.deleteAllPosts();
    }

    public void deleteAllPostsOfOneUser(String email) throws SQLException {
        postDAO.deleteAllPostsOfOneUser(email);
    }

    public String getPost(int postId, String email) throws SQLException, JsonProcessingException {
        Post post = postDAO.getPost(postId, email);
        if (post == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(post);
    }

    public String getAllPostsOfOneUser(String email) throws SQLException, JsonProcessingException {
        ArrayList<Post> posts = postDAO.getAllPostsOfOneUser(email);
        if (posts.isEmpty()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(posts);
    }

    public String getAllPosts() throws SQLException, JsonProcessingException {
        ArrayList<Post> posts = postDAO.getAllPosts();
        if (posts.isEmpty()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(postDAO.getAllPosts());
    }

    public boolean postExists(int postId) throws SQLException {
        return postDAO.isPostExist(postId);
    }
}
