package com.ros.lms.domain.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberStatusesTest {

    @Test
    void testGetVal_shouldReturnCorrectString() {
        assertThat(MemberStatuses.OVERDUE.getVal()).isEqualTo("OVERDUE");
        assertThat(MemberStatuses.HAS_LOAN.getVal()).isEqualTo("HAS-LOAN");
        assertThat(MemberStatuses.ELIGIBLE.getVal()).isEqualTo("ELIGIBLE");
    }

    @Test
    void testFromValue_shouldReturnCorrectEnum() {
        assertThat(MemberStatuses.fromValue("OVERDUE")).isEqualTo(MemberStatuses.OVERDUE);
        assertThat(MemberStatuses.fromValue("HAS-LOAN")).isEqualTo(MemberStatuses.HAS_LOAN);
        assertThat(MemberStatuses.fromValue("ELIGIBLE")).isEqualTo(MemberStatuses.ELIGIBLE);
    }

    @Test
    void testFromValue_shouldBeCaseInsensitive() {
        assertThat(MemberStatuses.fromValue("overdue")).isEqualTo(MemberStatuses.OVERDUE);
        assertThat(MemberStatuses.fromValue("has-loan")).isEqualTo(MemberStatuses.HAS_LOAN);
        assertThat(MemberStatuses.fromValue("eligible")).isEqualTo(MemberStatuses.ELIGIBLE);
    }

    @Test
    void testFromValue_shouldThrowExceptionForInvalidValue() {
        assertThatThrownBy(() -> MemberStatuses.fromValue("invalid-status"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown member status: invalid-status");
    }
}
