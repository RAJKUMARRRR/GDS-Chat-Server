package com.gds.chatserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Invalid OTP")
public class InvalidOTPException extends AuthenticationException {
    public InvalidOTPException(String s) {
        super(s);
    }
}
