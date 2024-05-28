package Server.DAO;

import Server.models.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactDAO {
    private final Connection connection;

    public ContactDAO() {
        connection = DataBaseConnection.getConnection();
    }

    public void saveContatcDetail(Contact contact, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO contact VALUES (?,?,?,?,?,?,?,?)");

        statement.setInt(1, id);
        statement.setString(2, contact.getEmail());
        statement.setString(3, contact.getPhoneNumber());
        statement.setString(4, contact.getNumberType());
        statement.setString(5, contact.getAddress());
        statement.setString(6, contact.getProfileLink());
        statement.setString(7, contact.getContactId());
        statement.setString(8, contact.getBirthdayAccess());

        statement.executeUpdate();

    }

    public void updateContact(Contact contact, int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE contact SET email = ? phonenumber = ? numbertype = ? address = ? profilelink = ? contactid = ? birthdayaccess = ? WHERE id = ?");

        statement.setString(1, contact.getEmail());
        statement.setString(2, contact.getPhoneNumber());
        statement.setString(3, contact.getNumberType());
        statement.setString(4, contact.getAddress());
        statement.setString(5, contact.getProfileLink());
        statement.setString(6, contact.getContactId());
        statement.setString(7, contact.getBirthdayAccess());
        statement.setInt(8, id);

        statement.executeUpdate();
    }

    public Contact getContact(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact  WHERE id = ?");

        statement.setInt(1, id);

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
}
