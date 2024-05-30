package Server.models;

public class Contact {

    enum phoneType {
        MOBILE,
        HOME,
        WORK
    }

    enum access {
        ME,
        CONTACTS,
        EVERYONE
    }

    private String profileLink;
    private String email;
    private String phoneNumber;
    private phoneType numberType;
    private String address;
    private String contactId; //eg telegram id/ instagram id
    private access birthdayAccess;

    public Contact(String profileLink, String email, String phoneNumber, String numberType, String address, String contactId, String birthdayAccess) {
        this.profileLink = profileLink;
        this.email = email;
        this.phoneNumber = phoneNumber;
        switch (numberType) {
            case "mobile":
                this.numberType = phoneType.MOBILE;
                break;
            case "home":
                this.numberType = phoneType.HOME;
                break;
            case "work":
                this.numberType = phoneType.WORK;
                break;
        }
        this.address = address;
        this.contactId = contactId;
        switch (birthdayAccess) {
            case "me":
                this.birthdayAccess = access.ME;
                break;
            case "contacts":
                this.birthdayAccess = access.CONTACTS;
                break;
            case "everyone":
                this.birthdayAccess = access.EVERYONE;
                break;
        }
    }

    public Contact() {

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
        return numberType.name().toLowerCase();
    }

    public void setNumberType(String numberType) {
        switch (numberType) {
            case "mobile":
                this.numberType = phoneType.MOBILE;
                break;
            case "home":
                this.numberType = phoneType.HOME;
                break;
            case "work":
                this.numberType = phoneType.WORK;
                break;
        }
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

    public String getBirthdayAccess() {
        return birthdayAccess.name().toLowerCase();
    }

    public void setBirthdayAccess(String birthdayAccess) {
        switch (birthdayAccess) {
            case "me":
                this.birthdayAccess = access.ME;
                break;
            case "contacts":
                this.birthdayAccess = access.CONTACTS;
                break;
            case "everyone":
                this.birthdayAccess = access.EVERYONE;
                break;
        }
    }
}
