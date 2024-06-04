package Server.DAO;

import Server.models.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.*;
import java.util.ArrayList;

public class CommentDAO {
    private final Connection connection;

    public CommentDAO() {
        connection = DataBaseConnection.getConnection();
    }

    public void saveComment(Comment comment) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO comments (user_email, comment_text, post_id) VALUES (?,?,?)");
        statement.setString(1, comment.getUser_email());
        statement.setString(2, comment.getComment());
        statement.setInt(3, comment.getPost_id());
        statement.executeUpdate();
    }

    public void updateComment(Comment comment) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE comments SET comment_text = ? WHERE comment_id = ?");
        statement.setString(1, comment.getComment());
        statement.setInt(2, comment.getComment_id());
        statement.executeUpdate();
    }

    public void deleteComment(int comment_id, String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM comments WHERE comment_id = ? AND user_email = ?");
        statement.setInt(1, comment_id);
        statement.setString(2, email);
        statement.executeUpdate();
    }

    public Comment getComment(int comment_id) throws SQLException, JsonProcessingException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM comments WHERE comment_id = ?");
        statement.setInt(1, comment_id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Comment comment = new Comment(resultSet.getString("user_email"), resultSet.getString("comment_text"), resultSet.getInt("post_id"), resultSet.getInt("comment_id"));
            return comment;
        } else return null;
    }

    public ArrayList<Comment> getAllCommentsOfOnePost (int post_id) throws SQLException, JsonProcessingException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM comments WHERE post_id = ?");

        statement.setInt(1, post_id);

        ResultSet resultSet = statement.executeQuery();
        ArrayList<Comment> comments = new ArrayList<>();

        while (resultSet.next()) {
            Comment comment = new Comment(resultSet.getString("user_email"), resultSet.getString("comment_text"), resultSet.getInt("post_id"), resultSet.getInt("comment_id"));
            comments.add(comment);
        }

        return comments;
    }

    public boolean isCommentExist(int comment_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM comments WHERE comment_id = ?");
        statement.setInt(1, comment_id);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public boolean isCommentExist(int comment_id, String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM comments WHERE comment_id = ? AND user_email = ?");
        statement.setInt(1, comment_id);
        statement.setString(2, email);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
