package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.Sex;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StaffTest {

    @Test
    void testBuilder_shouldSetAllFieldsCorrectly() {
        // Act
        Staff staff = new Staff.Builder()
                .id(1L)
                .governmentID("GOV123")
                .firstName("John")
                .middleName("A.")
                .lastName("Doe")
                .phone("1234567890")
                .sex(Sex.MALE)
                .email("john.doe@example.com")
                .username("johndoe")
                .build();

        // Assert
        assertThat(staff.getId()).isEqualTo(1L);
        assertThat(staff.getGovernmentID()).isEqualTo("GOV123");
        assertThat(staff.getFirstName()).isEqualTo("John");
        assertThat(staff.getMiddleName()).isEqualTo("A.");
        assertThat(staff.getLastName()).isEqualTo("Doe");
        assertThat(staff.getPhone()).isEqualTo("1234567890");
        assertThat(staff.getSex()).isEqualTo(Sex.MALE);
        assertThat(staff.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(staff.getUsername()).isEqualTo("johndoe");
    }

    @Test
    void testSettersAndGetters_shouldModifyAndReturnCorrectValues() {
        // Arrange
        Staff staff = new Staff();

        // Act
        staff.setId(5L);
        staff.setGovernmentID("GOV999");
        staff.setFirstName("Alice");
        staff.setMiddleName("B.");
        staff.setLastName("Smith");
        staff.setPhone("9876543210");
        staff.setSex(Sex.FEMALE);
        staff.setEmail("alice.smith@example.com");
        staff.setUsername("alicesmith");

        // Assert
        assertThat(staff.getId()).isEqualTo(5L);
        assertThat(staff.getGovernmentID()).isEqualTo("GOV999");
        assertThat(staff.getFirstName()).isEqualTo("Alice");
        assertThat(staff.getMiddleName()).isEqualTo("B.");
        assertThat(staff.getLastName()).isEqualTo("Smith");
        assertThat(staff.getPhone()).isEqualTo("9876543210");
        assertThat(staff.getSex()).isEqualTo(Sex.FEMALE);
        assertThat(staff.getEmail()).isEqualTo("alice.smith@example.com");
        assertThat(staff.getUsername()).isEqualTo("alicesmith");
    }

    @Test
    void testToString_shouldIncludeAllFieldValues() {
        // Arrange
        Staff staff = new Staff.Builder()
                .id(7L)
                .governmentID("GOV777")
                .firstName("Bob")
                .middleName("C.")
                .lastName("Brown")
                .phone("5551234567")
                .sex(Sex.MALE)
                .email("bob.brown@example.com")
                .username("bobbrown")
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
                "sex=M",
                "email='bob.brown@example.com'",
                "username='bobbrown'"
        );
    }

    @Test
    void testToString_shouldHandleNullValues() {
        Staff staff = new Staff.Builder()
                .id(1L)
                .governmentID("GOV123")
                .firstName("Alice")
                .middleName(null) // null value
                .lastName("Smith")
                .phone("123456789")
                .sex(null) // null value
                .email(null) // null value
                .username("alicesmith")
                .build();

        String str = staff.toString();

        assertThat(str).contains(
                "sex=null"
        );
    }
}
