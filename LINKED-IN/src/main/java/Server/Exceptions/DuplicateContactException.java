package Server.Exceptions;

public class DuplicateContactException extends Exception{
public DuplicateContactException() {
        super("Contact already exists!");
    }
}
