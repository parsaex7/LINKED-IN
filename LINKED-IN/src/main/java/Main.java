import Server.DAO.ContactDAO;
import Server.DAO.UserDao;
import Server.models.Contact;
import Server.models.User;

import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter Command: ");
            String command = scanner.nextLine();
            UserDao userDao = new UserDao();
            ContactDAO contactDAO = new ContactDAO();
            if (command.equals("add-user")) {
                User user = new User();
                System.out.println("Enter first name");
                String firstName = scanner.nextLine();
                System.out.println("Enter last Name");
                String lastName = scanner.nextLine();
                System.out.println("Enter email");
                String email = scanner.nextLine();
                System.out.println("Enter password");
                String passWord = scanner.nextLine();
                user.setName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPassword(passWord);
                userDao.saveUser(user);
            } else if (command.equals("add-contact")) {
                System.out.print("enter email:");
                String email = scanner.nextLine();
                System.out.print("enter phone number: ");
                scanner.nextLine();
                String phoneNumber = scanner.nextLine();
                System.out.print("enter phone type:");
                String phoneType = scanner.nextLine();
                System.out.print("enter birthday access:");
                String access = scanner.nextLine();
                User user = userDao.getUser(email);
                Contact contact = new Contact();
                contact.setEmail(user.getEmail());
                contact.setPhoneNumber(phoneNumber);
                contact.setNumberType(phoneType);
                contact.setBirthdayAccess(access);
                contactDAO.saveContatcDetail(contact);
            } else if (command.equals("edit-contact")) {
                System.out.println("enter email");
                String email = scanner.nextLine();
                Contact contact = contactDAO.getContact(email);
                System.out.println("enter new number");
                String number = scanner.next();
                contact.setPhoneNumber(number);
                contactDAO.updateContact(contact);
            } else if (command.equals("delete-users")) {
                userDao.deleteUsers();
            } else if (command.equals("update-user")) {
                String email;
                String passWord;
                System.out.println("please enter email and password");
                email = scanner.nextLine();
                passWord = scanner.nextLine();
                User user = userDao.getUser(email, passWord);
                if (user != null) {
                    System.out.println("Enter new firstname");
                    String newFirstName = scanner.nextLine();
                    System.out.println("Enter new lastname");
                    String newLastName = scanner.nextLine();
                    System.out.println("Enter new password");
                    String newPassWord = scanner.nextLine();
                    user.setName(newFirstName);
                    user.setPassword(newPassWord);
                    user.setLastName(newLastName);
                    userDao.updateUser(user, email);
                } else {
                    System.out.println("not-found");
                }

            } else if (command.equals("delete-user")) {
                String email;
                String passWord;
                System.out.println("please enter email and password");
                email = scanner.nextLine();
                passWord = scanner.nextLine();
                User toDelete = userDao.getUser(email, passWord);
                userDao.deleteUser(toDelete);
            } else if (command.equals("get-users")) {
                ArrayList<User> users;
                users = userDao.getUsers();
                for (User user : users) {
                    System.out.println(user);
                }
            } else if (command.equals("exit")) {
                return;
            }
        }
    }
}