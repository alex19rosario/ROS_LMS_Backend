package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.Sex;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StaffTest {

    @Test
    void testBuilder_shouldSetAllFieldsCorrectly() {
        // Arrange
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");

        // Act
        Staff staff = new Staff.Builder()
                .id(1L)
                .user(user)
                .governmentID("GOV123")
                .firstName("John")
                .middleName("A.")
                .lastName("Doe")
                .phone("1234567890")
                .sex(Sex.MALE)
                .build();

        // Assert
        assertThat(staff.getId()).isEqualTo(1L);
        assertThat(staff.getGovernmentID()).isEqualTo("GOV123");
        assertThat(staff.getFirstName()).isEqualTo("John");
        assertThat(staff.getMiddleName()).isEqualTo("A.");
        assertThat(staff.getLastName()).isEqualTo("Doe");
        assertThat(staff.getPhone()).isEqualTo("1234567890");
        assertThat(staff.getSex()).isEqualTo(Sex.MALE);
        assertThat(staff.getUser()).isEqualTo(user);
        assertThat(staff.getUser().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(staff.getUser().getUsername()).isEqualTo("johndoe");
    }

    @Test
    void testSettersAndGetters_shouldModifyAndReturnCorrectValues() {
        // Arrange
        Staff staff = new Staff();
        User user = new User();
        user.setEmail("alice.smith@example.com");
        user.setUsername("alicesmith");

        // Act
        staff.setId(5L);
        staff.setUser(user);
        staff.setGovernmentID("GOV999");
        staff.setFirstName("Alice");
        staff.setMiddleName("B.");
        staff.setLastName("Smith");
        staff.setPhone("9876543210");
        staff.setSex(Sex.FEMALE);

        // Assert
        assertThat(staff.getId()).isEqualTo(5L);
        assertThat(staff.getGovernmentID()).isEqualTo("GOV999");
        assertThat(staff.getFirstName()).isEqualTo("Alice");
        assertThat(staff.getMiddleName()).isEqualTo("B.");
        assertThat(staff.getLastName()).isEqualTo("Smith");
        assertThat(staff.getPhone()).isEqualTo("9876543210");
        assertThat(staff.getSex()).isEqualTo(Sex.FEMALE);
        assertThat(staff.getUser()).isEqualTo(user);
        assertThat(staff.getUser().getEmail()).isEqualTo("alice.smith@example.com");
        assertThat(staff.getUser().getUsername()).isEqualTo("alicesmith");
    }


    @Test
    void testToString_shouldIncludeAllFieldValues() {
        // Arrange
        User user = new User();
        user.setEmail("bob.brown@example.com");
        user.setUsername("bobbrown");

        Staff staff = new Staff.Builder()
                .id(7L)
                .user(user)
                .governmentID("GOV777")
                .firstName("Bob")
                .middleName("C.")
                .lastName("Brown")
                .phone("5551234567")
                .sex(Sex.MALE)
                .build();

        // Act
        String str = staff.toString();

        // Assert
        assertThat(str).contains(
                "Staff{",
                "id=7",
                "governmentID='GOV777'",
                "firstName='Bob'",
                "middleName='C.'",
                "lastName='Brown'",
                "phone='5551234567'",
                "sex=M",  // This should be the code, not the enum name
                "user='" + user.toString() + "'"
        );
    }

    @Test
    void testToString_shouldHandleNullValues() {
        // Arrange
        Staff staff = new Staff.Builder()
                .id(1L)
                .user(null) // null user
                .governmentID("GOV123")
                .firstName("Alice")
                .middleName(null) // null value
                .lastName("Smith")
                .phone("123456789")
                .sex(null) // null value
                .build();

        // Act
        String str = staff.toString();

        // Assert
        assertThat(str).contains(
                "sex=null",
                "user='null'"  // null user should be represented as 'null'
        );
    }
}
