package com.gds.chatserver.exceptions;

public class UserDoesNotExist extends Exception{
    public UserDoesNotExist(String s) {
        super(s);
    }
}
