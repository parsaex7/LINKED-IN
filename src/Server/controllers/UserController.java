package Server.controllers;

import Server.DAO.ContactDAO;
import Server.DAO.UserDao;
import Server.models.User;

import java.sql.Date;
import java.sql.SQLException;

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

    public void deleteUser(int id) throws SQLException {
        User user = userDao.getUser(id);
        userDao.deleteUser(user);
    }

    public void deleteUser(String email, String passWord) throws SQLException {
        User user = userDao.getUser(email, passWord);
        userDao.deleteUser(user);
    }

    public void UpdateUser(String firstName, String lastName, String email, String passWord, String country, String city, String additionalName, Date birthDay, Date registrationDate) throws SQLException {
        User user = new User(firstName, lastName, email, passWord, country, city, additionalName, birthDay, registrationDate);
        userDao.updateUser(user);
    }
}
