package com.ros.lms.domain.enums;

public enum Sex {
    MALE('M'),
    FEMALE('F');

    private final char code;

    Sex(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static Sex fromCode(char code) {
        return switch (code) {
            case 'M' -> MALE;
            case 'F' -> FEMALE;
            default -> throw new IllegalArgumentException("Unknown sex code: " + code);
        };
    }
}
