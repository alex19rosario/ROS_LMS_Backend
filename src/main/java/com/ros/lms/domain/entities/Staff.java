package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.Sex;
import jakarta.persistence.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Entity
@Table(name = "STAFFS")
public final class Staff extends PersonBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STAFF_ID")
    private long id;

    public Staff() {}

    public static class Builder{
        // Required parameters
        private long id;
        private User user;
        private String governmentID;
        private String firstName;
        private String middleName;
        private String lastName;
        private String phone;
        private Sex sex;

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder governmentID(String val) {
            governmentID = val;
            return this;
        }

        public Builder firstName(String val) {
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

        public Builder sex(Sex val) {
            sex = val;
            return this;
        }

        public Staff build() {
            return new Staff(this);
        }
    }

    private Staff(Builder builder) {
        id = builder.id;
        super.setUser(builder.user);
        super.setGovernmentID(builder.governmentID);
        super.setFirstName(builder.firstName);
        super.setMiddleName(builder.middleName);
        super.setLastName(builder.lastName);
        super.setPhone(builder.phone);
        super.setSex(builder.sex);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", user='" + super.getUser() + '\'' +
                ", governmentID='" + super.getGovernmentID() + '\'' +
                ", firstName='" + super.getFirstName() + '\'' +
                ", middleName='" + super.getMiddleName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", phone='" + super.getPhone() + '\'' +
                ", sex=" + super.getSex().getCode() +
                '}';
    }
}
