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

            server.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}