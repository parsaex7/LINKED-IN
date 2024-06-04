package Server.controllers;

import Server.DAO.HashTagDAO;
import Server.models.HashTag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.HashMap;

public class HashTagController {
    HashTagDAO hashTagDAO;

    public HashTagController() {
       hashTagDAO = new HashTagDAO();
    }
    public String getByHashTag(String tag) throws SQLException, JsonProcessingException {
        HashTag hashTag=new HashTag(tag);
        HashMap<Integer,String> result= hashTagDAO.getByHashTag(hashTag);
        if(result.isEmpty()){
            return null;
        }
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(result);
    }
}
