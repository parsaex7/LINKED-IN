package Server.DAO;

import Server.models.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LikeDAO {
    private final Connection connection;

    public LikeDAO() {
        connection = DataBaseConnection.getConnection();
    }

    public void saveLike(Like like) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO likes (user_email, post_id) VALUES (?,?)");

        statement.setString(1, like.getUser_email());
        statement.setInt(2, like.getPost_id());

        statement.executeUpdate();
    }

    public void deleteLike(Like like) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM likes WHERE post_id = ? AND user_email = ?");

        statement.setInt(1, like.getPost_id());
        statement.setString(2, like.getUser_email());

        statement.executeUpdate();
    }

    public ArrayList<Like> getAllLikes() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM likes");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like(resultSet.getString("user_email"), resultSet.getInt("post_id"));
            likes.add(like);
        }
        if (likes.isEmpty()) {
            return null;
        }
        return likes;
    }

    public ArrayList<Like> getAllLikesOfOnePost(int postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM likes WHERE post_id = ?");
        statement.setInt(1, postId);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like(resultSet.getString("user_email"), resultSet.getInt("post_id"));
            likes.add(like);
        }
        if (likes.isEmpty()) {
            return null;
        }
        return likes;
    }

    public ArrayList<Like> getAllLikesOfOnePerson(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM likes WHERE user_email = ?");
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like(resultSet.getString("user_email"), resultSet.getInt("post_id"));
            likes.add(like);
        }
        if (likes.isEmpty()) {
            return null;
        }
        return likes;
    }

    public boolean likeExists(String email, int postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM likes WHERE user_email = ? AND post_id = ?");
        statement.setString(1, email);
        statement.setInt(2, postId);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
