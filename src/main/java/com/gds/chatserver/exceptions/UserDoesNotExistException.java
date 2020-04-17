package com.gds.chatserver.exceptions;

public class UserDoesNotExistException extends Exception{
    public UserDoesNotExistException(String s) {
        super(s);
    }
}
