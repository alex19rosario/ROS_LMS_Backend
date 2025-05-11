package com.ros.lms.domain.enums;

public enum Roles {
    ADMIN("ROLE_ADMIN"),
    STAFF("ROLE_STAFF"),
    MEMBER("ROLE_MEMBER");

    private final String str;

    Roles(String str){
        this.str = str;
    }

    public String str(){
        return this.str;
    }
}
