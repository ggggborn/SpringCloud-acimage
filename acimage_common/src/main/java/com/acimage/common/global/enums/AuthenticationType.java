package com.acimage.common.global.enums;

public enum AuthenticationType {
    NONE(0),
    TOKEN_REQUIRED(1),
    ADMIN(2);

    private final int key;
    AuthenticationType(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}
