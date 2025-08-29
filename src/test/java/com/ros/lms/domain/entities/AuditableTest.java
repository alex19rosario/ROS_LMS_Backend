package com.ros.lms.domain.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditableTest {

    @Test
    void testCreatedAtGetterAndSetter() {
        Auditable auditable = new AuditableImpl();
        LocalDateTime now = LocalDateTime.now();

        auditable.setCreatedAt(now);

        assertEquals(now, auditable.getCreatedAt());
    }

    @Test
    void testUpdatedAtGetterAndSetter() {
        Auditable auditable = new AuditableImpl();
        LocalDateTime now = LocalDateTime.now();

        auditable.setUpdatedAt(now);

        assertEquals(now, auditable.getUpdatedAt());
    }

    // A simple concrete subclass for testing
    static class AuditableImpl extends Auditable {}
}
