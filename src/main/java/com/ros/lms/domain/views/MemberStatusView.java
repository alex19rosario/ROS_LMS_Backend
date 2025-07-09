package com.ros.lms.domain.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MEMBER_STATUS_VIEW")
public class MemberStatusView {

    @Id
    @Column(name = "MEMBER_ID")
    private long memberId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "MEMBER_STATUS")
    private String memberStatus;

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }
}
