package com.ros.lms.domain.enums;

public enum MemberStatuses {
    OVERDUE("OVERDUE"),
    HAS_LOAN("HAS-LOAN"),
    ELIGIBLE("ELIGIBLE");

    private final String val;

    MemberStatuses(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static MemberStatuses fromValue(String value) {
        for (MemberStatuses status : values()) {
            if (status.getVal().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown member status: " + value);
    }
}
