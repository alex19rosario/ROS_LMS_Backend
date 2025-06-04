package com.ros.lms.domain.enums;

public enum Routes {
    HEALTH_CHECK("/actuator/health"),
    LOGIN("/api/login"),
    IMAGES("/api/images/**"),
    BOOKS("/api/books/**"),
    GENRES("/api/genres/**"),
    MEMBERS("/api/members/**");

    private final String path;

    Routes(String path) {
        this.path = path;
    }

    public String val() {
        return path;
    }
}
