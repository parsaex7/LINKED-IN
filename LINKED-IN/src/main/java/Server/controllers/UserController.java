package Server.controllers;

import Server.DAO.ContactDAO;
import Server.DAO.UserDao;
import Server.models.Contact;
import Server.models.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public void createUser(String firstName, String lastName, String email, String passWord, String country, String city, String additionalName, String birthDay, String registrationDate) throws SQLException {
//        Date birthDaydate = Date.valueOf(birthDay);
//        Date registrationDate1 = Date.valueOf(registrationDate);
        User user = new User(firstName, lastName, email, passWord, country, city, additionalName, null, null);
        if (userDao.getUser(user.getEmail(), user.getPassword()) == null) {
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

    public String getUserByEmail(String email) throws SQLException, JsonProcessingException {
        User user = userDao.getUser(email);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }

    public String getUserByEmailAndPassword(String email, String password) throws SQLException, JsonProcessingException {
        User user = userDao.getUser(email, password);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }

    public String getContactByEmail(String email) throws SQLException, JsonProcessingException {
        Contact contact = contactDAO.getContact(email);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(contact);
    }

    public void deleteAllUsers() throws SQLException {
        userDao.deleteUsers();
    }

    public void deleteAllContacts() throws SQLException {
        contactDAO.deleteContacts();
    }

    public String getAllUsers() throws SQLException, JsonProcessingException {
        ArrayList<User> users = userDao.getUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(users);
    }

    public String getAllContacts() throws SQLException, JsonProcessingException {
        ArrayList<Contact> contacts = contactDAO.getContacts();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(contacts);
    }
}
