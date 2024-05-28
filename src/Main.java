import Server.DAO.ContactDAO;
import Server.DAO.UserDao;
import Server.models.Contact;
import Server.models.User;

import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Enter Command: ");
        Scanner scanner = new Scanner(System.in);
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
            System.out.print("enter id:");
            int id = scanner.nextInt();
            System.out.print("enter phone number: ");
            scanner.nextLine();
            String phoneNumber = scanner.nextLine();
            System.out.print("enter phone type:");
            String phoneType = scanner.nextLine();
            System.out.print("enter birthday access:");
            String access = scanner.nextLine();
            User user = userDao.getUser(id);
            Contact contact = new Contact();
            contact.setEmail(user.getEmail());
            contact.setPhoneNumber(phoneNumber);
            contact.setNumberType(phoneType);
            contact.setBirthdayAccess(access);
            contactDAO.saveContatcDetail(contact, id);
        } else if (command.equals("edit-contact")) {
            System.out.println("enter id");
            int id = scanner.nextInt();
            Contact contact = contactDAO.getContact(id);
            System.out.println("enter new number");
            String number = scanner.next();
            contact.setPhoneNumber(number);
            contactDAO.updateContact(contact, id);
        }
    }
}