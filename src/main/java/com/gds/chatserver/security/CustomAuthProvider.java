package com.gds.chatserver.security;

import com.gds.chatserver.exceptions.InvalidOTPException;
import com.gds.chatserver.service.OtpService;
import com.gds.chatserver.service.UserDetailsServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Configurable
public class CustomAuthProvider implements AuthenticationProvider {
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private OtpService otpService;

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String serverOtp = ""+otpService.getOtp(authentication.getPrincipal().toString());
                if(authentication.getCredentials().toString().equals(serverOtp)){
                    otpService.clearOTP(authentication.getPrincipal().toString());
                    UserDetails user = userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
                    return new UsernamePasswordAuthenticationToken( authentication.getPrincipal(),authentication.getCredentials(), this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
                }else{
                    throw  new ResponseStatusException(HttpStatus.FORBIDDEN,"Invalid OTP", new InvalidOTPException("Invalid OTP."));
                }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
