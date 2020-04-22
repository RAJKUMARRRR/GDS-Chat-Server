package com.gds.chatserver.controller;

import com.gds.chatserver.exceptions.UserDoesNotExistException;
import com.gds.chatserver.model.User;
import com.gds.chatserver.model.ValidateOtpRequest;
import com.gds.chatserver.repository.UserRepository;
import com.gds.chatserver.service.OtpService;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@RestController
@RequestMapping("/")
public class AuthController {
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserRepository userRepository;

    public static final String ACCOUNT_SID = "AC65df86c1d8fbdcbe9bd40eaa2c1256db";
    public static final String AUTH_TOKEN = "e935953aa49c30097fd3d872bed91b5a";
    private static final String DEV_APP_HASH = "TfMBu9iPSbu";
    private static final String RELEASE_APP_HASH = "tcDU+bWGDiV";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    @GetMapping("/auth/sendOTP")
    @CrossOrigin
    public ResponseEntity<Object> sendOtp(@RequestParam(name = "mobile") String mobileNumber) throws UserDoesNotExistException {
        User user = userRepository.findByPhone(mobileNumber);
        if(user==null){
            throw new UserDoesNotExistException("You are not registered.");
        }
        Message message = Message
                .creator(new PhoneNumber("+91"+mobileNumber), // to
                        new PhoneNumber("+15868001076"), // from
                        "Your OTP is "+otpService.generateOTP(mobileNumber)+"                " +
                                "                                  "+DEV_APP_HASH)
                .create();
        ResponseEntity<Object> responseEntity = new ResponseEntity<>("OTP sent successfully.", HttpStatus.OK);
        return responseEntity;
    }
}
