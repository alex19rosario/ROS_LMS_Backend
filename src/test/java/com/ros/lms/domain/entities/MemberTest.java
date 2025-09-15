package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.Sex;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;


class MemberTest {

    @Test
    void testBuilder_shouldSetAllFieldsCorrectly() {
        // Arrange
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);

        // Act
        Member member = new Member.Builder()
                .id(1L)
                .user(user)
                .governmentID("GOV123")
                .firstName("John")
                .middleName("A.")
                .lastName("Doe")
                .phone("1234567890")
                .dateOfBirth(dateOfBirth)
                .sex(Sex.MALE)
                .build();

        // Assert
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getGovernmentID()).isEqualTo("GOV123");
        assertThat(member.getFirstName()).isEqualTo("John");
        assertThat(member.getMiddleName()).isEqualTo("A.");
        assertThat(member.getLastName()).isEqualTo("Doe");
        assertThat(member.getPhone()).isEqualTo("1234567890");
        assertThat(member.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(member.getSex()).isEqualTo(Sex.MALE);
        assertThat(member.getUser()).isEqualTo(user);
        assertThat(member.getUser().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(member.getUser().getUsername()).isEqualTo("johndoe");
    }

    @Test
    void testSettersAndGetters_shouldModifyAndReturnCorrectValues() {
        // Arrange
        Member member = new Member();
        User user = new User();
        user.setEmail("alice.smith@example.com");
        user.setUsername("alicesmith");
        LocalDate dateOfBirth = LocalDate.of(1985, 8, 22);

        // Act
        member.setId(5L);
        member.setUser(user);
        member.setGovernmentID("GOV999");
        member.setFirstName("Alice");
        member.setMiddleName("B.");
        member.setLastName("Smith");
        member.setPhone("9876543210");
        member.setDateOfBirth(dateOfBirth);
        member.setSex(Sex.FEMALE);

        // Assert
        assertThat(member.getId()).isEqualTo(5L);
        assertThat(member.getGovernmentID()).isEqualTo("GOV999");
        assertThat(member.getFirstName()).isEqualTo("Alice");
        assertThat(member.getMiddleName()).isEqualTo("B.");
        assertThat(member.getLastName()).isEqualTo("Smith");
        assertThat(member.getPhone()).isEqualTo("9876543210");
        assertThat(member.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(member.getSex()).isEqualTo(Sex.FEMALE);
        assertThat(member.getUser()).isEqualTo(user);
        assertThat(member.getUser().getEmail()).isEqualTo("alice.smith@example.com");
        assertThat(member.getUser().getUsername()).isEqualTo("alicesmith");
    }

    @Test
    void testToString_shouldIncludeAllFieldValues() {
        // Arrange
        User user = new User();
        user.setEmail("bob.brown@example.com");
        user.setUsername("bobbrown");
        LocalDate dateOfBirth = LocalDate.of(1995, 3, 10);

        Member member = new Member.Builder()
                .id(7L)
                .user(user)
                .governmentID("GOV777")
                .firstName("Bob")
                .middleName("C.")
                .lastName("Brown")
                .phone("5551234567")
                .dateOfBirth(dateOfBirth)
                .sex(Sex.MALE)
                .build();

        // Act
        String str = member.toString();

        // Assert
        assertThat(str).contains(
                "Member{",
                "id=7",
                "governmentID='GOV777'",
                "firstName='Bob'",
                "middleName='C.'",
                "lastName='Brown'",
                "phone='5551234567'",
                "dateOfBirth=" + dateOfBirth,
                "sex=M",
                "user='" + user.toString() + "'"
        );
    }

    @Test
    void testToString_shouldHandleNullValues() {
        // Arrange
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");

        LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);

        Member member = new Member.Builder()
                .id(1L)
                .user(user) // null user
                .governmentID("GOV123")
                .firstName("Alice")
                .middleName(null) // null value
                .lastName("Smith")
                .phone("123456789")
                .dateOfBirth(dateOfBirth) // null date of birth
                .sex(Sex.FEMALE) // null value
                .build();

        // Act
        String str = member.toString();

        // Assert
        assertThat(str).contains(
                "middleName='null'"
        );
    }
}
