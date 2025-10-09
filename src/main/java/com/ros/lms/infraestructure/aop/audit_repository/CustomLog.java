package com.ros.lms.infraestructure.aop.audit_repository;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@DynamoDbBean
public class CustomLog {

    private String logId;
    private String timeStamp;
    private String description;
    private String staffUsername;
    private String actionType;
    private String memberUsername;
    private String bookIsbn;
    private Long loanId;

    public CustomLog() {}

    public static class Builder {
        // Required parameters
        private final String logID;
        private final String timeStamp;

        // Optional parameters
        private String description;
        private String staffUsername;
        private String actionType;
        private String memberUsername;
        private String bookIsbn;
        private Long loanId;

        public Builder() {
            logID = UUID.randomUUID().toString();
            timeStamp = Instant.now().toString();
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder staffUsername(String val) {
            staffUsername = val;
            return this;
        }

        public Builder actionType(String val) {
            actionType = val;
            return this;
        }

        public Builder memberUsername(String val) {
            memberUsername = val;
            return this;
        }

        public Builder bookIsbn(String val) {
            bookIsbn = val;
            return this;
        }

        public Builder loanId(Long val) {
            loanId = val;
            return this;
        }

        public CustomLog build() {
            return new CustomLog(this);
        }

    }

    private CustomLog(Builder builder) {
        logId = builder.logID;
        timeStamp = builder.timeStamp;
        description = builder.description;
        staffUsername = builder.staffUsername;
        actionType = builder.actionType;
        memberUsername = builder.memberUsername;
        bookIsbn = builder.bookIsbn;
        loanId = builder.loanId;
    }

    @DynamoDbPartitionKey
    public String getLogId() {
        return logId;
    }

    @DynamoDbSortKey
    public String getTimeStamp() {
        return timeStamp;
    }

    public String getDescription() {
        return description;
    }

    public String getStaffUsername() {
        return staffUsername;
    }

    public String getActionType() {
        return actionType;
    }

    public String getMemberUsername() {
        return memberUsername;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStaffUsername(String staffUsername) {
        this.staffUsername = staffUsername;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setMemberUsername(String memberUsername) {
        this.memberUsername = memberUsername;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomLog)) return false;
        CustomLog that = (CustomLog) o;
        return Objects.equals(description, that.description)
                && Objects.equals(staffUsername, that.staffUsername)
                && Objects.equals(actionType, that.actionType)
                && Objects.equals(memberUsername, that.memberUsername)
                && Objects.equals(bookIsbn, that.bookIsbn)
                && Objects.equals(loanId, that.loanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, staffUsername, actionType, memberUsername, bookIsbn, loanId);
    }

    @Override
    public String toString() {
        return "CustomLog{" +
                "logId='" + logId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", description='" + description + '\'' +
                ", staffUsername='" + staffUsername + '\'' +
                ", actionType='" + actionType + '\'' +
                ", memberUsername='" + memberUsername + '\'' +
                ", bookIsbn='" + bookIsbn + '\'' +
                ", loanId=" + loanId +
                '}';
    }
}
