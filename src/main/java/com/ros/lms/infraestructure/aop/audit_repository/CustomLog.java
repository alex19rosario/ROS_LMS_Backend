package com.ros.lms.infraestructure.aop.audit_repository;

/**
 *
 * @param description
 * @param staffUsername
 * @param actionType
 * @param memberUsername
 * @param bookIsbn
 * @param loanId
 */
public record CustomLog(
        String description,
        String staffUsername,
        String actionType,
        String memberUsername,
        String bookIsbn,
        Long loanId) {

    public static class Builder {
        private String description;
        private String staffUsername;
        private String actionType;
        private String memberUsername;
        private String bookIsbn;
        private Long loanId;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder staffUsername(String staffUsername) {
            this.staffUsername = staffUsername;
            return this;
        }

        public Builder actionType(String actionType) {
            this.actionType = actionType;
            return this;
        }

        public Builder memberUsername(String memberUsername) {
            this.memberUsername = memberUsername;
            return this;
        }

        public Builder bookIsbn(String bookIsbn) {
            this.bookIsbn = bookIsbn;
            return this;
        }

        public Builder loanId(Long loanId) {
            this.loanId = loanId;
            return this;
        }

        public CustomLog build() {
            return new CustomLog(
                    description,
                    staffUsername,
                    actionType,
                    memberUsername,
                    bookIsbn,
                    loanId
            );
        }
    }
}
