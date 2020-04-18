package com.gds.chatserver.exceptions;

public class SystemMessageNotFoundException extends RuntimeException {
    public SystemMessageNotFoundException(String s) {
        super(s);
    }
}
