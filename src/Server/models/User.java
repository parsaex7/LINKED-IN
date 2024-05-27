package Server.models;

import java.sql.Date;

public class User {

    private int id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String additionalName;
    private Contact contactDetails;
    private String country;
    private String city;
    private Date birthDate;
    private Date registrationDate;
    private Education educationDetails;
    private Job jobDetails;

    public User(int id, String name, String lastName, String email, String password, String additionalName, Contact contactDetails, String country, String city, Date birthDate, Education educationDetails, Job jobDetails) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.additionalName = additionalName;
        this.contactDetails = contactDetails;
        this.country = country;
        this.city = city;
        this.birthDate = birthDate;
        this.educationDetails = educationDetails;
        this.jobDetails = jobDetails;
        registrationDate = new Date(System.currentTimeMillis());
    }

    public User(int id, String name, String lastName, String email, String password, String additionalName, Contact contactDetails, String country, String city, Date birthDate, Education educationDetails, Job jobDetails, Date registrationDate) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.additionalName = additionalName;
        this.contactDetails = contactDetails;
        this.country = country;
        this.city = city;
        this.birthDate = birthDate;
        this.educationDetails = educationDetails;
        this.jobDetails = jobDetails;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public Contact getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(Contact contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Education getEducationDetails() {
        return educationDetails;
    }

    public void setEducationDetails(Education educationDetails) {
        this.educationDetails = educationDetails;
    }

    public Job getJobDetails() {
        return jobDetails;
    }

    public void setJobDetails(Job jobDetails) {
        this.jobDetails = jobDetails;
    }
}
