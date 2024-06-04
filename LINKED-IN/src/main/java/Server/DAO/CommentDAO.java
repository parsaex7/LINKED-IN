package Server.DAO;

import Server.models.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.*;
import java.util.ArrayList;

public class CommentDAO {
    private final Connection connection;

    public CommentDAO()  {
        connection = DataBaseConnection.getConnection();
        try {
            createCommentTable();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void createCommentTable() throws SQLException {
        PreparedStatement statement= connection.prepareStatement("CREATE TABLE IF NOT EXISTS `comments` (\n" +
                "  `comment_id` int NOT NULL AUTO_INCREMENT,\n" +
                "  `post_id` int DEFAULT NULL,\n" +
                "  `user_email` varchar(40) DEFAULT NULL,\n" +
                "  `comment_text` varchar(1250) NOT NULL,\n" +
                "  PRIMARY KEY (`comment_id`),\n" +
                "  KEY `post_id` (`post_id`),\n" +
                "  KEY `user_email` (`user_email`),\n" +
                "  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE,\n" +
                "  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_email`) REFERENCES `users` (`email`) ON DELETE CASCADE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
        statement.execute();
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
