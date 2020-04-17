package com.gds.chatserver.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final String SEND_OTP_URL = "/auth/sendOTP";
    public static final String VALIDATE_OTP_URL = "/auth/validateOTP";
}
