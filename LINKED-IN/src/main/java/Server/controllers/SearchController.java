package Server.controllers;

import Server.DAO.PostDAO;
import Server.DAO.SearchDAO;
import Server.DAO.UserDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class SearchController {
    private SearchDAO searchDAO;

    public SearchController() {
        searchDAO = new SearchDAO();
    }

    public String searchUser(String search) throws SQLException, JsonProcessingException {
        ArrayList<String> emails = searchDAO.searchUser(search);
        if (emails.isEmpty()) {
            return null;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(emails);
        }
    }

}
