package Server.Exceptions;

public class ContactNotExistException extends Exception{
public ContactNotExistException() {
        super("Contact does not exist!");
    }
}
