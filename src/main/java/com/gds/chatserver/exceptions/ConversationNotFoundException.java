package com.gds.chatserver.exceptions;

public class ConversationNotFoundException extends RuntimeException {
    public ConversationNotFoundException(String s) {
        super(s);
    }
}
