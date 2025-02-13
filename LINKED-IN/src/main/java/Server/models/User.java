package Server.models;

import java.sql.Date;

public class User {

    private String name;
    private String lastName;
    private String email;
    private String password;
    private String additionalName;
    private String country;
    private String city;
    private Date birthDate;
    private Date registrationDate;
//firstName, lastName, email, passWord, country, city, additionalName
    public User(String name, String lastName, String email, String password, String country, String city, String additionalName, Date birthDate) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.additionalName = additionalName;
        this.country = country;
        this.city = city;
        this.birthDate = birthDate;
        registrationDate = new Date(System.currentTimeMillis());
    }

    public User(String name, String lastName, String email, String password, String country, String city, String additionalName, Date birthDate, Date registrationDate) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.additionalName = additionalName;
        this.country = country;
        this.city = city;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
    }

    public User() {

    }

    public User(String firstName, String lastName, String email, String passWord) {
        this.name = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = passWord;
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

    public String toString() {
        String result;
        result = "first name:" + name + "  last name:" + lastName + "  email:" + email + "  pass word:" + password;
        return result;
    }


}
