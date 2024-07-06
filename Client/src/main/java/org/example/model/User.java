package org.example.model;

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
        private String token;
        private String hire;

        public User(String email, String password, String token, String name, String lastName) {
            this.email = email;
            this.password = password;
            this.token = token;
            this.name = name;
            this.lastName = lastName;
        }
        public User(String email, String password, String token, String name, String lastName, String country, String city, String additionalName) {
            this.email = email;
            this.password = password;
            this.token = token;
            this.name = name;
            this.lastName = lastName;
            this.country = country;
            this.city = city;
            this.additionalName = additionalName;
        }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
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

    public void setEmail(String email) {
        this.email = email;
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

    public void setToken(String token) {
        this.token = token;
    }

    public String getHire() {
        return hire;
    }

    public void setHire(String hire) {
        this.hire = hire;
    }
}
