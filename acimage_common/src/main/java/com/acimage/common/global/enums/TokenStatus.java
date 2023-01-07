package com.acimage.common.global.enums;

public enum TokenStatus {
    PASS_TOKEN_VERIFY(0),
    NULL(1),
    EXPIRE(2),
    INVALID(3),
    MISMATCH_IP(4),
    VALID(5);
    private final int key;

    TokenStatus(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}
