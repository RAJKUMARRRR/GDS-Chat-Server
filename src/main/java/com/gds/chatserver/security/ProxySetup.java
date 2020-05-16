package com.gds.chatserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.core.env.Environment;

public class ProxySetup {
        @Autowired
        private Environment environment;

        @PostConstruct
        public void init() throws MalformedURLException {
            URL fixie = new URL("http://"+environment.getProperty("FIXIE_SOCKS_HOST"));
            String[] fixieUserInfo = fixie.getUserInfo().split(":");
            String fixieUser = fixieUserInfo[0];
            String fixiePassword = fixieUserInfo[1];
            System.setProperty("socksProxyHost", fixie.getHost());
            Authenticator.setDefault(new ProxyAuthenticator(fixieUser, fixiePassword));
        }
}
