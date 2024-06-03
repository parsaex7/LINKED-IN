package Server.controllers;

import Server.DAO.CommentDAO;
import Server.models.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class CommentController {
    private CommentDAO commentDAO;

    public CommentController() {
        commentDAO = new CommentDAO();
    }

    public boolean isCommentExist(int comment_id) throws SQLException {
        return commentDAO.isCommentExist(comment_id);
    }

    public boolean isCommentExist(int comment_id, String email) throws SQLException {
        return commentDAO.isCommentExist(comment_id, email);
    }

    public void createComment(String user_email, String comment, int post_id) throws SQLException {
        Comment newComment = new Comment(user_email, comment, post_id);
        commentDAO.saveComment(newComment);
    }

    public boolean updateComment(String user_email, String comment,int comment_id) throws SQLException {
        Comment updatedComment = new Comment(user_email, comment, 0, 0);
        updatedComment.setComment_id(comment_id);
        if (isCommentExist(comment_id)) {
            commentDAO.updateComment(updatedComment);
            return true;
        } else return false;
    }

    public boolean deleteComment(int comment_id, String email) throws SQLException {
        if (isCommentExist(comment_id, email)) {
            commentDAO.deleteComment(comment_id, email);
            return true;
        } else return false;
    }

    public String getComment(int comment_id) throws SQLException, JsonProcessingException {
        Comment comment = commentDAO.getComment(comment_id);
        if (comment == null) {
            return null;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(comment);
        }
    }

    public String getAllCommentsOfOnePost(int post_id) throws SQLException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Comment> comments = commentDAO.getAllCommentsOfOnePost(post_id);
        if (comments.isEmpty()) {
            return null;
        } else {
            return objectMapper.writeValueAsString(comments);
        }
    }
}
