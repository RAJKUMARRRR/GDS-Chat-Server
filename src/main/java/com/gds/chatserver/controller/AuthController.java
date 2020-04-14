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

import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    public static final String ACCOUNT_SID = "AC65df86c1d8fbdcbe9bd40eaa2c1256db";
    public static final String AUTH_TOKEN = "de859aeaacbdeb24e2ecf8ab88bc082b";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @GetMapping("/sendOtp")
    @CrossOrigin
    public ResponseEntity<Object> sendOtp(@RequestParam(name = "mobile") String mobileNumber){
        Message message = Message
                .creator(new PhoneNumber("+91"+mobileNumber), // to
                        new PhoneNumber("+15868001076"), // from
                        "Your OTP is "+otpService.generateOTP(mobileNumber))
                .create();
        ResponseEntity<Object> responseEntity = new ResponseEntity<>("OTP sent successfully.", HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping(value ="/validateOtp")
    @CrossOrigin
    public String validateOtp(@RequestBody ValidateOtpRequest validateOtpRequest){
        final String SUCCESS = "Entered Otp is valid";
        final String FAIL = "Entered Otp is NOT valid. Please Retry!";
        if(validateOtpRequest.getOtp() >= 0){
            int serverOtp = otpService.getOtp(validateOtpRequest.getMobileNumber());
            if(serverOtp > 0){
                if(validateOtpRequest.getOtp() == serverOtp){
                    otpService.clearOTP(validateOtpRequest.getMobileNumber());
                    return SUCCESS;
                }else{
                    return FAIL;
                }
            }else {
                return FAIL;
            }
        }else {
            return FAIL;
        }
    }
}
