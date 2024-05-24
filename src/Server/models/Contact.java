package Server.models;

public class Contact {

    private String profileLink;
    private String email;
    private String phoneNumber;
    private String numberType;
    private String address;
    private String contactId; //eg telegram id/ instagram id

    public Contact(String profileLink, String email, String phoneNumber, String numberType, String address, String contactId) {
        this.profileLink = profileLink;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.numberType = numberType;
        this.address = address;
        this.contactId = contactId;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
