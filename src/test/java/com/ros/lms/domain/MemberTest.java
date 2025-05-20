package com.ros.lms.domain;

import com.ros.lms.domain.entities.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    private Member member;
    private final String testGovernmentID = "ID-123456";
    private final String testFirstName = "John";
    private final String testMiddleName = "Robert";
    private final String testLastName = "Doe";
    private final String testPhone = "+1234567890";
    private final byte testAge = 30;
    private final char testSex = 'M';
    private final String testEmail = "john.doe@example.com";
    private final String testUsername = "johndoe";

    @BeforeEach
    void setUp() {
        member = new Member(testGovernmentID, testFirstName, testMiddleName,
                testLastName, testPhone, testAge, testSex,
                testEmail, testUsername);
    }

    @Test
    void testGettersAndSetters() {
        // Test initial values from constructor
        assertEquals(testGovernmentID, member.getGovernmentID());
        assertEquals(testFirstName, member.getFirstName());
        assertEquals(testMiddleName, member.getMiddleName());
        assertEquals(testLastName, member.getLastName());
        assertEquals(testPhone, member.getPhone());
        assertEquals(testAge, member.getAge());
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

        byte newAge = 25;
        member.setAge(newAge);
        assertEquals(newAge, member.getAge());

        char newSex = 'F';
        member.setSex(newSex);
        assertEquals(newSex, member.getSex());

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
        Member paramMember = new Member(testGovernmentID, testFirstName, testMiddleName,
                testLastName, testPhone, testAge, testSex,
                testEmail, testUsername);
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
        assertEquals(0, emptyMember.getAge()); // default value for byte
        assertEquals('\u0000', emptyMember.getSex()); // default value for char
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
        Member nullMember = new Member(null, null, null, null, null, (byte)0, '\u0000', null, null);
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
    void testAgeBoundaries() {
        // Test minimum age
        member.setAge(Byte.MIN_VALUE);
        assertEquals(Byte.MIN_VALUE, member.getAge());

        // Test maximum age
        member.setAge(Byte.MAX_VALUE);
        assertEquals(Byte.MAX_VALUE, member.getAge());

        // Test zero age
        member.setAge((byte)0);
        assertEquals(0, member.getAge());
    }

    @Test
    void testSexValues() {
        member.setSex('M');
        assertEquals('M', member.getSex());

        member.setSex('F');
        assertEquals('F', member.getSex());

        member.setSex('O'); // Other
        assertEquals('O', member.getSex());

        member.setSex('X'); // Unknown or unspecified
        assertEquals('X', member.getSex());
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
        assertTrue(toStringResult.contains("age=" + testAge));
        assertTrue(toStringResult.contains("sex=" + testSex));
        assertTrue(toStringResult.contains("email='" + testEmail + "'"));
        assertTrue(toStringResult.contains("username=" + testUsername));

        // Test with null values
        member.setMiddleName(null);
        member.setEmail(null);
        toStringResult = member.toString();
        assertTrue(toStringResult.contains("middleName='null'"));
        assertTrue(toStringResult.contains("email='null'"));
    }
}
