package com.ros.lms.domain.entities;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonBaseTest {

    @Test
    void personBase_shouldBeSealedAndPermitOnlyStaffAndMember() {
        Class<PersonBase> clazz = PersonBase.class;

        assertTrue(clazz.isSealed(), "PersonBase should be a sealed class");

        Class<?>[] permitted = clazz.getPermittedSubclasses();
        assertNotNull(permitted, "Permitted subclasses should not be null");
        assertEquals(2, permitted.length, "PersonBase should have exactly two permitted subclasses");

        assertTrue(
                contains(permitted, Staff.class),
                "PersonBase should permit Staff"
        );
        assertTrue(
                contains(permitted, Member.class),
                "PersonBase should permit Member"
        );
    }

    private boolean contains(Class<?>[] array, Class<?> target) {
        for (Class<?> c : array) {
            if (c.equals(target)) return true;
        }
        return false;
    }
}
