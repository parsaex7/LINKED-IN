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
}
