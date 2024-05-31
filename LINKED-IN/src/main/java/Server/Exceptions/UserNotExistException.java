package Server.Exceptions;

public class UserNotExistException extends Exception{
public UserNotExistException() {
        super("User does not exist!");
    }
}
