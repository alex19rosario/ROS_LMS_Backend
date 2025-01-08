package com.ros.aop.audit_entity;

public class CustomLog {

    private final String description;
    private final Long staffId;
    private final String actionType;
    private final Long memberId;
    private final Long bookId;
    private final Long loanId;

    public static class Builder {
        // Required parameters

        private final String actionType;

        // Optional parameters
        private String description = null;
        private Long staffId = null;
        private Long memberId = null;
        private Long bookId = null;
        private Long loanId = null;

        public Builder(String actionType){
            this.actionType = actionType;
        }

        public Builder description(String val){
            description = val;
            return this;
        }

        public Builder staffId(long val){
            staffId = val;
            return this;
        }

        public Builder memberId(long val){
            memberId = val;
            return this;
        }

        public Builder bookId(long val){
            bookId = val;
            return this;
        }

        public Builder loanId(long val){
            loanId = val;
            return this;
        }

        public CustomLog build(){
            return new CustomLog(this);
        }
    }

    private CustomLog(Builder builder){
        description = builder.description;
        staffId = builder.staffId;
        actionType = builder.actionType;
        memberId = builder.memberId;
        bookId = builder.bookId;
        loanId = builder.loanId;
    }

    public String getDescription() {
        return description;
    }

    public Long getStaffId() {
        return staffId;
    }

    public String getActionType() {
        return actionType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getLoanId() {
        return loanId;
    }

    @Override
    public String toString() {
        return "CustomLog{" +
                "description='" + description + '\'' +
                ", staffId=" + staffId +
                ", actionType='" + actionType + '\'' +
                ", memberId=" + memberId +
                ", bookId=" + bookId +
                ", loanId=" + loanId +
                '}';
    }
}
