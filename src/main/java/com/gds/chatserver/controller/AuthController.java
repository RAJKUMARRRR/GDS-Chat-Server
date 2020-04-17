package com.gds.chatserver.controller;

import com.gds.chatserver.model.ValidateOtpRequest;
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
    public static final String ACCOUNT_SID = "AC65df86c1d8fbdcbe9bd40eaa2c1256db";
    public static final String AUTH_TOKEN = "4e80df1a4c7cf1e37b1acf73d9bb8a7a";
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    @GetMapping("/auth/sendOTP")
    @CrossOrigin
    public ResponseEntity<Object> sendOtp(@RequestParam(name = "mobile") String mobileNumber){
        Message message = Message
                .creator(new PhoneNumber("+91"+mobileNumber), // to
                        new PhoneNumber("+15868001076"), // from
                        "Your OTP is "+otpService.generateOTP(mobileNumber)+"                " +
                                "                                  TfMBu9iPSbu")
                .create();
        ResponseEntity<Object> responseEntity = new ResponseEntity<>("OTP sent successfully.", HttpStatus.OK);
        return responseEntity;
    }
}
