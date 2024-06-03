package Server.controllers;

import Server.DAO.FollowDAO;
import Server.Exceptions.UserNotExistException;
import Server.models.Follow;

import java.sql.SQLException;

public class FollowController {
    private final FollowDAO followDAO=new FollowDAO();
    public void follow(String email1,String email2) throws SQLException, UserNotExistException {
        //email 1 wants to follow email2
        Follow follow=new Follow(email1,email2);
        followDAO.follow(follow);
    }
    public void unFollow(String email1,String email2) throws SQLException, UserNotExistException {
        //email 1 wants to unfollow email2
        Follow follow=new Follow(email1,email2);
        followDAO.unFollow(follow);
    }
}
