import Server.DAO.UserDao;
import Server.models.User;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("Enter Comand: ");
        Scanner scanner = new Scanner(System.in);
        String comand=scanner.nextLine();
        UserDao userDao=new UserDao();
        if(comand.equals("add-user")){
            User toadd=new User();
            System.out.println("Enter first name");
            String fristName=scanner.nextLine();
            System.out.println("Enter last Name");
            String lastName=scanner.nextLine();
            System.out.println("Enter email");
            String email=scanner.nextLine();
            System.out.println("Enter password");
            String passWord=scanner.nextLine();
            toadd.setName(fristName);
            toadd.setLastName(lastName);
            toadd.setEmail(email);
            toadd.setPassword(passWord);
            userDao.saveUser(toadd);
        }
    }
}