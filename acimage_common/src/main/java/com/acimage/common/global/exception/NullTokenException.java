package com.acimage.common.global.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

public class NullTokenException extends JWTVerificationException {

    public NullTokenException(String message){super(message);}
}
