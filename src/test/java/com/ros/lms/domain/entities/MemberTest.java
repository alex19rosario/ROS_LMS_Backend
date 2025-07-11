package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.Sex;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    private Member member;
    private final String testGovernmentID = "ID-123456";
    private final String testFirstName = "John";
    private final String testMiddleName = "Robert";
    private final String testLastName = "Doe";
    private final String testPhone = "+1234567890";
    private final LocalDate testDateOfBirth = LocalDate.of(1999, 9, 17);
    private final Sex testSex = Sex.MALE;
    private final String testEmail = "john.doe@example.com";
    private final String testUsername = "johndoe";

    private static Validator validator;

    @BeforeEach
    void setUp() {
        member = new Member.Builder()
            .governmentID(testGovernmentID)
            .firstName(testFirstName)
            .middleName(testMiddleName)
            .lastName(testLastName)
            .phone(testPhone)
            .dateOfBirth(testDateOfBirth)
            .sex(testSex)
            .email(testEmail)
            .username(testUsername)
            .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGettersAndSetters() {
        // Test initial values from constructor
        assertEquals(testGovernmentID, member.getGovernmentID());
        assertEquals(testFirstName, member.getFirstName());
        assertEquals(testMiddleName, member.getMiddleName());
        assertEquals(testLastName, member.getLastName());
        assertEquals(testPhone, member.getPhone());
        assertEquals(testDateOfBirth, member.getDateOfBirth());
        assertEquals(testSex, member.getSex());
        assertEquals(testEmail, member.getEmail());
        assertEquals(testUsername, member.getUsername());

        // Test setters
        long newId = 1L;
        member.setId(newId);
        assertEquals(newId, member.getId());

        String newGovernmentID = "ID-987654";
        member.setGovernmentID(newGovernmentID);
        assertEquals(newGovernmentID, member.getGovernmentID());

        String newFirstName = "Jane";
        member.setFirstName(newFirstName);
        assertEquals(newFirstName, member.getFirstName());

        // Test all other setters similarly
        String newMiddleName = "Marie";
        member.setMiddleName(newMiddleName);
        assertEquals(newMiddleName, member.getMiddleName());

        String newLastName = "Smith";
        member.setLastName(newLastName);
        assertEquals(newLastName, member.getLastName());

        String newPhone = "+9876543210";
        member.setPhone(newPhone);
        assertEquals(newPhone, member.getPhone());

        LocalDate newDateOfBirth = LocalDate.of(1985, 3, 7);
        member.setDateOfBirth(newDateOfBirth);
        assertEquals(newDateOfBirth, member.getDateOfBirth());

        member.setSex(Sex.FEMALE);
        assertEquals(Sex.FEMALE, member.getSex());

        String newEmail = "jane.smith@example.com";
        member.setEmail(newEmail);
        assertEquals(newEmail, member.getEmail());

        String newUsername = "janesmith";
        member.setUsername(newUsername);
        assertEquals(newUsername, member.getUsername());
    }

    @Test
    void testConstructors() {
        // Test parameterized constructor
        Member paramMember = new Member.Builder()
                .governmentID(testGovernmentID)
                .firstName(testFirstName)
                .middleName(testMiddleName)
                .lastName(testLastName)
                .phone(testPhone)
                .dateOfBirth(testDateOfBirth)
                .sex(testSex)
                .email(testEmail)
                .username(testUsername)
                .build();
        assertEquals(testFirstName, paramMember.getFirstName());
        assertEquals(testMiddleName, paramMember.getMiddleName());
        assertEquals(testLastName, paramMember.getLastName());
        assertEquals(0, paramMember.getId()); // default value for long

        // Test no-arg constructor
        Member emptyMember = new Member();
        assertEquals(0, emptyMember.getId());
        assertNull(emptyMember.getGovernmentID());
        assertNull(emptyMember.getFirstName());
        assertNull(emptyMember.getMiddleName());
        assertNull(emptyMember.getLastName());
        assertNull(emptyMember.getPhone());
        assertNull(emptyMember.getDateOfBirth());
        assertNull(emptyMember.getSex()); // default value for char
        assertNull(emptyMember.getEmail());
        assertNull(emptyMember.getUsername());
    }

    @Test
    void testNullValues() {
        member.setGovernmentID(null);
        assertNull(member.getGovernmentID());

        member.setFirstName(null);
        assertNull(member.getFirstName());

        member.setMiddleName(null);
        assertNull(member.getMiddleName());

        member.setLastName(null);
        assertNull(member.getLastName());

        member.setPhone(null);
        assertNull(member.getPhone());

        member.setEmail(null);
        assertNull(member.getEmail());

        member.setUsername(null);
        assertNull(member.getUsername());

        // Test constructor with null values
        Member nullMember = new Member.Builder()
                .governmentID(null)
                .firstName(null)
                .middleName(null)
                .lastName(null)
                .phone(null)
                .dateOfBirth(null)
                .sex(null)
                .email(null)
                .username(null)
                .build();
        assertNull(nullMember.getGovernmentID());
        assertNull(nullMember.getFirstName());
        assertNull(nullMember.getMiddleName());
        assertNull(nullMember.getLastName());
        assertNull(nullMember.getPhone());
        assertNull(nullMember.getEmail());
        assertNull(nullMember.getUsername());
    }

    @Test
    void testEmptyStrings() {
        member.setGovernmentID("");
        assertEquals("", member.getGovernmentID());

        member.setFirstName(" ");
        assertEquals(" ", member.getFirstName());

        member.setMiddleName("\t");
        assertEquals("\t", member.getMiddleName());

        member.setLastName("\n");
        assertEquals("\n", member.getLastName());

        member.setPhone("");
        assertEquals("", member.getPhone());

        member.setEmail(" ");
        assertEquals(" ", member.getEmail());

        member.setUsername("");
        assertEquals("", member.getUsername());
    }

    @Test
    void testSexValues() {
        member.setSex(Sex.MALE);
        assertEquals(Sex.MALE, member.getSex());

        member.setSex(Sex.FEMALE);
        assertEquals(Sex.FEMALE, member.getSex());
    }

    @Test
    void dateOfBirth_shouldFailValidation_whenDateIsInFuture() {
        // Given
        Member invalidMember = new Member.Builder()
                .governmentID("123456789")
                .firstName("John")
                .middleName("A")
                .lastName("Doe")
                .phone("1234567890")
                .dateOfBirth(LocalDate.now().plusDays(1))
                .sex(Sex.MALE)
                .email("john.doe@example.com")
                .username("johndoe")
                .build();

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(invalidMember);

        // Then
        assertFalse(violations.isEmpty(), "Validation should fail for future date of birth.");

        boolean hasPastViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateOfBirth")
                        && v.getMessage().equals("Date of birth must be in the past."));

        assertTrue(hasPastViolation, "Expected @Past constraint violation on dateOfBirth.");
    }

    @Test
    void dateOfBirth_shouldPassValidation_whenDateIsInPast() {
        // Given
        Member validMember = new Member.Builder()
                .governmentID("987654321")
                .firstName("Jane")
                .middleName("B")
                .lastName("Smith")
                .phone("0987654321")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .sex(Sex.FEMALE)
                .email("jane.smith@example.com")
                .username("janesmith")
                .build();

        // When
        Set<ConstraintViolation<Member>> violations = validator.validate(validMember);

        // Then
        assertTrue(violations.isEmpty(), "Validation should pass for valid past date of birth.");
    }

    @Test
    void testToString() {
        // Test with all values set
        String toStringResult = member.toString();
        assertTrue(toStringResult.contains("id=0")); // default value
        assertTrue(toStringResult.contains("governmentID='" + testGovernmentID + "'"));
        assertTrue(toStringResult.contains("firstName='" + testFirstName + "'"));
        assertTrue(toStringResult.contains("middleName='" + testMiddleName + "'"));
        assertTrue(toStringResult.contains("lastName='" + testLastName + "'"));
        assertTrue(toStringResult.contains("phone='" + testPhone + "'"));
        assertTrue(toStringResult.contains("dateOfBirth=" + testDateOfBirth));
        assertTrue(toStringResult.contains("sex=" + testSex.getCode()));
        assertTrue(toStringResult.contains("email='" + testEmail + "'"));
        assertTrue(toStringResult.contains("username=" + testUsername));

        // Test with null values
        member.setMiddleName(null);
        member.setEmail(null);
        toStringResult = member.toString();
        assertTrue(toStringResult.contains("middleName='null'"));
        assertTrue(toStringResult.contains("email='null'"));
    }

    @Test
    void testToString_shouldHandleNullValues() {
        Member newMember = new Member.Builder()
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

        String str = newMember.toString();

        assertThat(str).contains(
                "sex=null"
        );
    }

    @Test
    void testBuilderSetsAllFieldsCorrectly() {
        LocalDate dob = LocalDate.of(1990, 5, 20);

        Member builtMember = new Member.Builder()
                .id(42L)
                .governmentID("GOV-XYZ123")
                .firstName("Alice")
                .middleName("B.")
                .lastName("Wonderland")
                .phone("555-1234")
                .dateOfBirth(dob)
                .sex(Sex.FEMALE)
                .email("alice@example.com")
                .username("alicew")
                .build();

        assertEquals(42L, builtMember.getId());
        assertEquals("GOV-XYZ123", builtMember.getGovernmentID());
        assertEquals("Alice", builtMember.getFirstName());
        assertEquals("B.", builtMember.getMiddleName());
        assertEquals("Wonderland", builtMember.getLastName());
        assertEquals("555-1234", builtMember.getPhone());
        assertEquals(dob, builtMember.getDateOfBirth());
        assertEquals(Sex.FEMALE, builtMember.getSex());
        assertEquals("alice@example.com", builtMember.getEmail());
        assertEquals("alicew", builtMember.getUsername());
    }

}
