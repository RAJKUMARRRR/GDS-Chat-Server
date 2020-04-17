package com.gds.chatserver.exceptions;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String s) {
        super(s);
    }
}
