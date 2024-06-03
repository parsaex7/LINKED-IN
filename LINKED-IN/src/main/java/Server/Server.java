package Server;

import Server.HttpHandlers.*;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            server.createContext("/user", new UserHandler()); //for sign up
            server.createContext("/login", new LoginHandler()); //for login
            server.createContext("/contact", new ContactHandler()); //for make/update/delete/get Contact Details
            server.createContext("/education", new EducationHandler()); //for make/update/delete/get Education Details
            server.createContext("/post", new PostHandler()); //for make/update/delete/get Post
            server.createContext("/like", new LikeHandler()); //for like/unlike post and get all likes of one post and get all likes of one user
            server.createContext("/comment", new CommentHandler()); //for make/update/delete/get Comment
            server.createContext("/follow", new FollowHandler()); //for follow/unfollow user and get all followers of one user and get all following of one user
            server.createContext("/search", new SearchHandler()); //for searching among users
            server.createContext("/follow",new FollowHandler());//for follow /unfollow/get followers/get followings


            server.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}