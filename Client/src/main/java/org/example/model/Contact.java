package org.example.model;

public class Contact {
    private String phoneNumber;
    private String numberType;
    private String address;
    private String contactId; //eg telegram id/ instagram id
    private String birthdayAccess;

    public Contact(String phoneNumber, String numberType, String address, String contactId, String birthdayAccess) {
        this.phoneNumber = phoneNumber;
        this.numberType = numberType;
        this.address = address;
        this.contactId = contactId;
        this.birthdayAccess = birthdayAccess;
    }

    public Contact() {

    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getBirthdayAccess() {
        return birthdayAccess;
    }

    public void setBirthdayAccess(String birthdayAccess) {
        this.birthdayAccess = birthdayAccess;
    }

}
