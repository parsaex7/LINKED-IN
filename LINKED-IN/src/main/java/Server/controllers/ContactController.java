package Server.controllers;

import Server.DAO.ContactDAO;
import Server.models.Contact;

import java.sql.SQLException;
import java.util.ArrayList;

public class ContactController {
    private final ContactDAO contactDAO;
    public ContactController(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }
    public void createContact(String profileLink, String email, String phoneNumber, String numberType, String address, String contactId, String birthdayAccess) throws SQLException {
        Contact toAdd=new Contact(profileLink,email,phoneNumber,numberType,address,contactId,birthdayAccess);
        contactDAO.saveContatcDetail(toAdd);
    }
    public void upDate(Contact contact,String newPhoneNumber,String newAddress,String newBirthdayAccess,String newNumberType,String newProfileLink) throws SQLException {
        contact.setPhoneNumber(newPhoneNumber);
        contact.setAddress(newAddress);
        contact.setBirthdayAccess(newBirthdayAccess);
        contact.setNumberType(newNumberType);
        contact.setProfileLink(newProfileLink);
        contactDAO.updateContact(contact);
    }
    public Contact getContact(String email) throws SQLException {
        Contact toreturn=contactDAO.getContact(email);
        return toreturn;
    }
    public void deleteContact(String email) throws SQLException {
        contactDAO.deleteContact(email);
    }
    public void deleteContacts() throws SQLException {
        contactDAO.deleteContacts();
    }
    public ArrayList<Contact> getContacts() throws SQLException {
       return contactDAO.getContacts();
    }
}
