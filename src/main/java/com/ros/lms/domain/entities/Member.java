package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.Sex;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import org.jspecify.annotations.NullMarked;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDate;

@NullMarked
@Entity
@Table(name = "MEMBERS")
public class Member extends PersonBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private long id;

    @NotNull
    @Column(name = "DATE_OF_BIRTH")
    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    public Member() {}

    public static class Builder {
        // Required parameters
        private long id;
        private User user;
        private String governmentID;
        private String firstName;
        private String middleName;
        private String lastName;
        private String phone;
        private LocalDate dateOfBirth;
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
        public Builder dateOfBirth(LocalDate val) {
            dateOfBirth = val;
            return this;
        }
        public Builder sex(Sex val) {
            sex = val;
            return this;
        }
        public Member build() {
            return new Member(this);
        }
    }

    private Member(Builder builder) {
        id = builder.id;
        super.setUser(builder.user);
        super.setGovernmentID(builder.governmentID);
        super.setFirstName(builder.firstName);
        super.setMiddleName(builder.middleName);
        super.setLastName(builder.lastName);
        super.setPhone(builder.phone);
        dateOfBirth = builder.dateOfBirth;
        super.setSex(builder.sex);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", user='" + super.getUser() + '\'' +
                ", governmentID='" + super.getGovernmentID() + '\'' +
                ", firstName='" + super.getFirstName() + '\'' +
                ", middleName='" + super.getMiddleName() + '\'' +
                ", lastName='" + super.getLastName() + '\'' +
                ", phone='" + super.getPhone() + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", sex=" + super.getSex().getCode() +
                '}';
    }
}
