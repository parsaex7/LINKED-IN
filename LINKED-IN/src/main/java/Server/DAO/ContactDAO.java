package Server.DAO;

import Server.models.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ContactDAO {
    private final Connection connection;

    public ContactDAO() {
        connection = DataBaseConnection.getConnection();
    }

    public void saveContatcDetail(Contact contact) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO contact (email, phonenumber, numbertype, address, profilelink, contactid, birthdayaccess) VALUES (?,?,?,?,?,?,?)");

        statement.setString(1, contact.getEmail());
        statement.setString(2, contact.getPhoneNumber());
        statement.setString(3, contact.getNumberType());
        statement.setString(4, contact.getAddress());
        statement.setString(5, contact.getProfileLink());
        statement.setString(6, contact.getContactId());
        statement.setString(7, contact.getBirthdayAccess());

        statement.executeUpdate();

    }
public void updateContact(Contact contact) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("UPDATE contact SET phonenumber = ?, numbertype = ?, address = ?, profilelink = ?, contactid = ?, birthdayaccess = ? WHERE email = ?");

    statement.setString(1, contact.getPhoneNumber());
    statement.setString(2, contact.getNumberType());
    statement.setString(3, contact.getAddress());
    statement.setString(4, contact.getProfileLink());
    statement.setString(5, contact.getContactId());
    statement.setString(6, contact.getBirthdayAccess());
    statement.setString(7, contact.getEmail());
    statement.executeUpdate();
}

    public Contact getContact(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact  WHERE email = ?");

        statement.setString(1, email);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Contact contact = new Contact();
            contact.setEmail(resultSet.getString("email"));
            contact.setPhoneNumber(resultSet.getString("phonenumber"));
            contact.setNumberType(resultSet.getString("numbertype"));
            contact.setAddress(resultSet.getString("address"));
            contact.setProfileLink(resultSet.getString("profilelink"));
            contact.setContactId(resultSet.getString("contactid"));
            contact.setBirthdayAccess(resultSet.getString("birthdayaccess"));
            return contact;
        } else {
            return null;
        }
    }

    public void deleteContact(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM contact WHERE email = ?");

        statement.setString(1, email);

        statement.executeUpdate();
    }

    public void deleteContacts() throws SQLException{
        PreparedStatement statement = connection.prepareStatement("DELETE FROM contact");
        statement.execute();
    }

    public ArrayList<Contact> getContacts() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact");

        ResultSet resultSet = statement.executeQuery();

        ArrayList<Contact> contacts = new ArrayList<>();
        while (resultSet.next()) {
            Contact contact = new Contact();
            contact.setEmail(resultSet.getString("email"));
            contact.setPhoneNumber(resultSet.getString("phonenumber"));
            contact.setNumberType(resultSet.getString("numbertype"));
            contact.setAddress(resultSet.getString("address"));
            contact.setProfileLink(resultSet.getString("profilelink"));
            contact.setContactId(resultSet.getString("contactid"));
            contact.setBirthdayAccess(resultSet.getString("birthdayaccess"));
            contacts.add(contact);
        }

        return contacts;
    }
}
