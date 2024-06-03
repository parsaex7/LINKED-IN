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
        if (userDao.getUser(user.getEmail()) != null) {
            throw new DuplicateUserException();
        } else {
            userDao.saveUser(user);
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


    public String getAllUsers() throws SQLException, JsonProcessingException {
        ArrayList<User> users = userDao.getUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(users);
    }
}
