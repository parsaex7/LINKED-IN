package Server.controllers;

import Server.DAO.ContactDAO;
import Server.DAO.UserDao;
import Server.Exceptions.ContactNotExistException;
import Server.Exceptions.DuplicateContactException;
import Server.Exceptions.DuplicateUserException;
import Server.Exceptions.UserNotExistException;
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

    public void createUser(String firstName, String lastName, String email, String passWord, String country, String city, String additionalName, Date birthDay, Date registrationDate) throws SQLException, DuplicateUserException {
        User user = new User(firstName, lastName, email, passWord, country, city, additionalName, birthDay, registrationDate);
        if (userDao.getUser(user.getEmail(), user.getPassword()) != null) {
            throw new DuplicateUserException();
        } else {
            userDao.saveUser(user);
        }
    }

    public void createContact(String email, String phoneNumber, String numberType, String address, String profileLink, String contactId, String birthdayAccess) throws SQLException, UserNotExistException, DuplicateContactException {
        User user = userDao.getUser(email);
        if (user == null) {
            throw new UserNotExistException();
        } else {
            Contact contact = new Contact(profileLink, email, phoneNumber, numberType, address, contactId, birthdayAccess);
            if (contactDAO.getContact(email) != null) {
                throw new DuplicateContactException();
            } else {
                contactDAO.saveContatcDetail(contact);
            }
        }
    }

    public void deleteUserByEmail(String email) throws SQLException, UserNotExistException {
        User user = userDao.getUser(email);
        if (user == null) {
            throw new UserNotExistException();
        } else {
            userDao.deleteUser(user);

        }
    }

    public void deleteUserByEmailAndPassword(String email, String passWord) throws SQLException, UserNotExistException {
        User user = userDao.getUser(email, passWord);
        if (user == null) {
            throw new UserNotExistException();
        } else {
            userDao.deleteUser(user);

        }
    }

    public void deleteContactByEmail(String email) throws SQLException {
        contactDAO.deleteContact(email);
    }


    public void UpdateUser(String firstName, String lastName, String email, String passWord, String country, String city, String additionalName, Date birthDay, Date registrationDate) throws SQLException, UserNotExistException {
        if (userDao.getUser(email) != null) {
            User user = new User(firstName, lastName, email, passWord, country, city, additionalName, birthDay, registrationDate);
            userDao.updateUser(user);
        } else {
            throw new UserNotExistException();
        }
    }


    public void updateContact(String email, String phoneNumber, String numberType, String address, String profileLink, String contactId, String birthdayAccess) throws SQLException, ContactNotExistException {
        if (contactDAO.getContact(email) != null) {
        Contact contact = new Contact(profileLink, email, phoneNumber, numberType, address, contactId, birthdayAccess);
        contactDAO.updateContact(contact);
        } else {
            throw new ContactNotExistException();
        }
    }

    public String getUserByEmail(String email) throws SQLException, JsonProcessingException, UserNotExistException {
        User user = userDao.getUser(email);
        if (user == null) {
            throw new UserNotExistException();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(user);
        }
    }

    public String getUserByEmailAndPassword(String email, String password) throws SQLException, JsonProcessingException {
        User user = userDao.getUser(email, password);
        if (user == null) {
            return null;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(user);
        }
    }

    public String getContactByEmail(String email) throws SQLException, JsonProcessingException, ContactNotExistException {
        Contact contact = contactDAO.getContact(email);
        if (contact == null) {
            throw new ContactNotExistException();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(contact);
        }
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
