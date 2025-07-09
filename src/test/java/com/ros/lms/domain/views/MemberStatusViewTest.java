package com.ros.lms.domain.views;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberStatusViewTest {

    @Test
    void testGettersAndSetters() {
        MemberStatusView view = new MemberStatusView();

        view.setMemberId(123L);
        view.setFirstName("John");
        view.setLastName("Doe");
        view.setMemberStatus("HAS-LOAN");

        assertThat(view.getMemberId()).isEqualTo(123L);
        assertThat(view.getFirstName()).isEqualTo("John");
        assertThat(view.getLastName()).isEqualTo("Doe");
        assertThat(view.getMemberStatus()).isEqualTo("HAS-LOAN");
    }

    @Test
    void testDefaultValues_shouldBeNullOrZero() {
        MemberStatusView view = new MemberStatusView();

        assertThat(view.getMemberId()).isZero(); // primitive long
        assertThat(view.getFirstName()).isNull();
        assertThat(view.getLastName()).isNull();
        assertThat(view.getMemberStatus()).isNull();
    }

    @Test
    void testNullValues() {
        MemberStatusView view = new MemberStatusView();

        view.setFirstName(null);
        view.setLastName(null);
        view.setMemberStatus(null);

        assertThat(view.getFirstName()).isNull();
        assertThat(view.getLastName()).isNull();
        assertThat(view.getMemberStatus()).isNull();
    }

    @Test
    void testEmptyStrings() {
        MemberStatusView view = new MemberStatusView();

        view.setFirstName("");
        view.setLastName(" ");
        view.setMemberStatus("\t");

        assertThat(view.getFirstName()).isEqualTo("");
        assertThat(view.getLastName()).isEqualTo(" ");
        assertThat(view.getMemberStatus()).isEqualTo("\t");
    }
}
