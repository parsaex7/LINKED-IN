package Server.DAO;

import Server.models.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PostDAO {
    private final Connection connection;

    public PostDAO() {
        connection = DataBaseConnection.getConnection();
    }

    public void savePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (user_email, post_text) VALUES (?,?)");

        statement.setString(1, post.getSenderEmail());
        statement.setString(2, post.getMessage());

        statement.executeUpdate();
    }

    public void updatePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE posts SET post_text = ? WHERE post_id = ?");

        statement.setString(1, post.getMessage());
        statement.setInt(2, post.getPostId());

        statement.executeUpdate();
    }

    public void deletePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM posts WHERE post_id = ? AND user_email = ?");

        statement.setInt(1, post.getPostId());
        statement.setString(2, post.getSenderEmail());

        statement.executeUpdate();
    }

    public void deleteAllPosts() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM posts");

        statement.executeUpdate();
    }

    public void deleteAllPostsOfOneUser(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM posts WHERE user_email = ?");

        statement.setString(1, email);

        statement.executeUpdate();
    }

    public Post getPost(int postId, String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE post_id = ? AND user_email = ?");

        statement.setInt(1, postId);
        statement.setString(2, email);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Post post = new Post(resultSet.getString("post_text"), resultSet.getString("user_email"));
            post.setPostId(postId);
            return post;
        } else {
            return null;
        }
    }

    public ArrayList<Post> getAllPostsOfOneUser(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE user_email = ?");

        statement.setString(1, email);

        ResultSet resultSet = statement.executeQuery();

        ArrayList<Post> posts = new ArrayList<>();

        while (resultSet.next()) {
            Post post = new Post(resultSet.getInt("post_id"), resultSet.getString("post_text"), resultSet.getString("user_email"));
            posts.add(post);
        }

        return posts;
    }

    public ArrayList<Post> getAllPosts() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts");

        ResultSet resultSet = statement.executeQuery();

        ArrayList<Post> posts = new ArrayList<>();

        while (resultSet.next()) {
            Post post = new Post(resultSet.getInt("post_id"), resultSet.getString("post_text"), resultSet.getString("user_email"));
            posts.add(post);
        }

        return posts;
    }

    public boolean isPostExist(int postId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts WHERE post_id = ?");

        statement.setInt(1, postId);

        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }
}
