package com.gds.chatserver.service;

import com.gds.chatserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private OtpService otpService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.gds.chatserver.model.User applicationUser = userRepository.findByPhone(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getPhone(), bCryptPasswordEncoder.encode(otpService.getOtp(username)+""), emptyList());
    }

    public com.gds.chatserver.model.User getLoggedInUser(){
        return userRepository.findByPhone(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
}