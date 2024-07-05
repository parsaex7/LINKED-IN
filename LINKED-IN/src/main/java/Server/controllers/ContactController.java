package Server.controllers;

import Server.DAO.ContactDAO;
import Server.models.Contact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;

public class ContactController {
    private final ContactDAO contactDAO = new ContactDAO();

    public void createContact(String profileLink, String email, String phoneNumber, String numberType, String address, String contactId, String birthdayAccess) throws SQLException {
        Contact contact = new Contact(profileLink, email, phoneNumber, numberType, address, contactId, birthdayAccess);
        contactDAO.saveContatcDetail(contact);
    }

    public void updateContact(String profileLink, String email, String phoneNumber, String numberType, String address, String contactId, String birthdayAccess) throws SQLException {
        Contact contact = new Contact(profileLink, email, phoneNumber, numberType, address, contactId, birthdayAccess);
        if (contactDAO.getContact(email) == null) {
            contactDAO.saveContatcDetail(contact);
            return;
        }
        contactDAO.updateContact(contact);
    }

    public String getContact(String email) throws SQLException, JsonProcessingException {
        Contact contact = contactDAO.getContact(email);
        if (contact == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(contact);
    }

    public void deleteContact(String email) throws SQLException {
        contactDAO.deleteContact(email);
    }

    public void deleteContacts() throws SQLException {
        contactDAO.deleteContacts();
    }

    public String getContacts() throws SQLException, JsonProcessingException {
        ArrayList<Contact> contacts = contactDAO.getContacts();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(contacts);
    }
}
