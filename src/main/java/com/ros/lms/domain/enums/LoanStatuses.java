package com.ros.lms.domain.enums;

public enum LoanStatuses {
    LOANED("LOANED"),
    RETURNED("RETURNED"),
    OVERDUE("OVERDUE"),
    RETURNED_LATE("RETURNED-LATE"),
    LOST("LOST");

    private final String val;

    LoanStatuses(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }
}
