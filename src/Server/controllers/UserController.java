package Server.controllers;

import Server.DAO.ContactDAO;
import Server.DAO.UserDao;
import Server.models.Contact;
import Server.models.User;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {

    private final UserDao userDao;
    private final ContactDAO contactDAO;

    public UserController() {
        userDao = new UserDao();
        contactDAO = new ContactDAO();
    }

    public void createUser(String firstName, String lastName, String email, String passWord, String country, String city, String additionalName, Date birthDay, Date registrationDate) throws SQLException {
        User user = new User(firstName, lastName, email, passWord, country, city, additionalName, birthDay, registrationDate);
        if (userDao.getUser(user.getEmail(), user.getPassword()) != null) {
            userDao.saveUser(user);
        } else {
            userDao.updateUser(user);
        }
    }

    public void createContact(String email, String phoneNumber, String numberType, String address, String profileLink, String contactId, String birthdayAccess) throws SQLException {
        User user = userDao.getUser(email);
        if (user == null) {
            //TODO: throw exception
        } else {
            Contact contact = new Contact(profileLink, email, phoneNumber, numberType, address, contactId, birthdayAccess);
            if (contactDAO.getContact(email) != null) {
                contactDAO.updateContact(contact);
            } else {
                contactDAO.saveContatcDetail(contact);
            }
        }
    }

    public void deleteUserByEmail(String email) throws SQLException {
        User user = userDao.getUser(email);
        userDao.deleteUser(user);
    }

    public void deleteUserByEmailAndPassword(String email, String passWord) throws SQLException {
        User user = userDao.getUser(email, passWord);
        userDao.deleteUser(user);
    }

    public void deleteContactByEmail(String email) throws SQLException {
        contactDAO.deleteContact(email);
    }


    public void UpdateUser(String firstName, String lastName, String email, String passWord, String country, String city, String additionalName, Date birthDay, Date registrationDate) throws SQLException {
        User user = new User(firstName, lastName, email, passWord, country, city, additionalName, birthDay, registrationDate);
        userDao.updateUser(user);
    }


    public void updateContact(String email, String phoneNumber, String numberType, String address, String profileLink, String contactId, String birthdayAccess) throws SQLException {
        Contact contact = new Contact(profileLink, email, phoneNumber, numberType, address, contactId, birthdayAccess);
        contactDAO.updateContact(contact);
    }

    public User getUserByEmail(String email) throws SQLException {
        return userDao.getUser(email);
    }

    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        return userDao.getUser(email, password);
    }

    public Contact getContactByEmail(String email) throws SQLException {
        return contactDAO.getContact(email);
    }

    public void deleteAllUsers() throws SQLException {
        userDao.deleteUsers();
    }

    public void deleteAllContacts() throws SQLException {
        contactDAO.deleteContacts();
    }

    public ArrayList<User> getAllUsers() throws SQLException {
        return userDao.getUsers();
    }

    public ArrayList<Contact> getAllContacts() throws SQLException{
        return contactDAO.getContacts();
    }
}
