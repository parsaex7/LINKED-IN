package Server.models;

public class ContactInformation {
    private String contactEmail;
    private String profileLink;
    private String phoneNumber;
    private String address;
    private String birthDate;
    private String telegramId;
    private String skypeId;

    public ContactInformation(String contactEmail,String profileLink, String phoneNumber, String address, String birthDate, String telegramId, String skypeId) {
        this.contactEmail = contactEmail;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthDate = birthDate;
        this.telegramId = telegramId;
        this.skypeId = skypeId;
        this.profileLink=profileLink;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
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

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }
}
