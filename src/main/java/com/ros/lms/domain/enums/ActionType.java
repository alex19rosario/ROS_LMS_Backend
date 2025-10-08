package com.ros.lms.domain.enums;

public enum ActionType {
    NEW_BOOK_WAS_ADDED("NEW BOOK WAS ADDED"),
    A_BOOK_WAS_BORROWED("A BOOK WAS BORROWED"),
    A_BOOK_WAS_RETURNED("A BOOK WAS RETURNED"),
    A_BOOK_WAS_RETURNED_LATE("A BOOK WAS RETURNED LATE"),
    OVERDUE("OVERDUE"),
    NEW_MEMBER_WAS_ADDED("NEW MEMBER WAS ADDED"),
    NEW_STAFF_WAS_ADDED("NEW STAFF WAS ADDED"),
    ERROR("ERROR");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ActionType fromValue(String value) {
        for (ActionType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ActionType: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}

