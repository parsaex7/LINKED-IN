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
        try {
            createPostTable();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void createPostTable() throws SQLException {
        PreparedStatement statement= connection.prepareStatement("CREATE TABLE IF NOT EXISTS `posts` (\n" +
                "  `post_id` int NOT NULL AUTO_INCREMENT,\n" +
                "  `user_email` varchar(40) DEFAULT NULL,\n" +
                "  `post_text` varchar(3000) NOT NULL,\n" +
                "  PRIMARY KEY (`post_id`),\n" +
                "  KEY `user_email` (`user_email`),\n" +
                "  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_email`) REFERENCES `users` (`email`) ON DELETE CASCADE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
        statement.execute();
    }

    public void savePost(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (user_email, post_text) VALUES (?,?)");

        statement.setString(1, post.getSenderEmail());
        statement.setString(2, post.getMessage());

        statement.executeUpdate();
    }

    public void saveFile(Post post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (user_email, post_text, file_path) VALUES (?,?,?)");

        statement.setString(1, post.getSenderEmail());
        statement.setString(2, post.getMessage());
        statement.setString(3, post.getFile_path());

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
            Post post = new Post(resultSet.getInt("post_id"), resultSet.getString("post_text"), resultSet.getString("user_email"), resultSet.getString("file_path"));
            posts.add(post);
        }

        return posts;
    }

    public ArrayList<Post> getAllPosts() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM posts");

        ResultSet resultSet = statement.executeQuery();

        ArrayList<Post> posts = new ArrayList<>();

        while (resultSet.next()) {
            Post post = new Post(resultSet.getInt("post_id"), resultSet.getString("post_text"), resultSet.getString("user_email"), resultSet.getString("file_path"));
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
