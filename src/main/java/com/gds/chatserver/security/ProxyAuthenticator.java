package com.gds.chatserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyAuthenticator extends Authenticator {
    private final PasswordAuthentication passwordAuthentication;

    public ProxyAuthenticator(String user, String password) {
        passwordAuthentication = new PasswordAuthentication(user, password.toCharArray());
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return passwordAuthentication;
    }
}