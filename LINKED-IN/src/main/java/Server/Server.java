package Server;

import Server.HttpHandlers.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/user", new UserHandler());
            server.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
