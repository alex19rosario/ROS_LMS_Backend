package com.ros.lms.domain.enums;

public enum Roles {
    ADMIN("ROLE_ADMIN", "ADMIN"),
    STAFF("ROLE_STAFF", "STAFF"),
    MEMBER("ROLE_MEMBER", "MEMBER");

    private final String str;
    private final String val;

    Roles(String str, String val){
        this.str = str;
        this.val = val;
    }

    public String str(){
        return this.str;
    }
    public String val() {
        return this.val;
    }
}
