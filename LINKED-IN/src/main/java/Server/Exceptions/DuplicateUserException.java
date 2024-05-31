package Server.Exceptions;

public class DuplicateUserException extends Exception{
public DuplicateUserException() {
        super("User already exists!");
    }
}
