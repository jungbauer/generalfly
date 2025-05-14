package com.jungbauer.generalfly.utils;

public enum UserRoles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    DEMO("ROLE_DEMO");

    private final String text;

    UserRoles(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
