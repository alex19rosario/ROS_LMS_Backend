package com.ros.lms.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "STAFFS")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STAFF_ID")
    private long id;
    @Column(name = "GOVERNMENT_ID")
    private String governmentID;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "SEX")
    private char sex;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "USERNAME")
    private String username;

    public Staff() {}

    public static class Builder{
        // Required parameters
        private long id;
        private String governmentID;
        private String firstName;
        private String middleName;
        private String lastName;
        private String phone;
        private char sex;
        private String email;
        private String username;

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder governmentID(String val) {
            governmentID = val;
            return this;
        }

        public Builder firsName(String val) {
            firstName = val;
            return this;
        }

        public Builder middleName(String val) {
            middleName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder sex(char val) {
            sex = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }
    }

    private Staff(Builder builder) {
        id = builder.id;
        governmentID = builder.governmentID;
        firstName = builder.firstName;
        middleName = builder.middleName;
        lastName = builder.lastName;
        phone = builder.phone;
        sex = builder.sex;
        email = builder.email;
        username = builder.username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGovernmentID() {
        return governmentID;
    }

    public void setGovernmentID(String governmentID) {
        this.governmentID = governmentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", governmentID='" + governmentID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
