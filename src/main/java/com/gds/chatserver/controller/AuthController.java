package com.gds.chatserver.controller;

import com.gds.chatserver.exceptions.UserDoesNotExistException;
import com.gds.chatserver.model.SendOTPRequest;
import com.gds.chatserver.model.User;
import com.gds.chatserver.model.ValidateOtpRequest;
import com.gds.chatserver.repository.UserRepository;
import com.gds.chatserver.service.OtpService;
import com.gds.chatserver.utils.ParameterStringBuilder;
import com.twilio.Twilio;
import io.netty.handler.codec.base64.Base64Encoder;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/")
public class AuthController {
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserRepository userRepository;

    private static final String DEV_APP_HASH = "TfMBu9iPSbu";
    private static final String RELEASE_APP_HASH = "tcDU+bWGDiV";
    private String HASH_KEY="tcDU+bWGDiV";

    private Environment environment;
    private String TRANSMIT_SMS_API_KEY;
    private String TRANSMIT_SMS_API_SECRET;
    private String TRANSMIT_SMS_URL;

    /*static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }*/

    @Autowired
    AuthController(Environment environment){
        this.environment = environment;
        //String ACCOUNT_SID = environment.getProperty("TWILLIO_ACCOUNT_SID");//environment.getProperty("TWILLIO_ACCOUND_SID");
        //String AUTH_TOKEN = environment.getProperty("TWILLIO_AUTH_TOKEN");
        HASH_KEY = environment.getProperty("OTP_APP_HASH_KEY");
        //Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
        TRANSMIT_SMS_API_KEY = environment.getProperty("TRANSMIT_SMS_API_KEY");
        TRANSMIT_SMS_API_SECRET = environment.getProperty("TRANSMIT_SMS_API_SECRET");
        TRANSMIT_SMS_URL = environment.getProperty("TRANSMIT_SMS_URL");
    }

    @PostMapping("/auth/sendOTP")
    @CrossOrigin
    public ResponseEntity<Object> sendOtp(@RequestBody SendOTPRequest sendOTPRequest) throws UserDoesNotExistException, IOException {
        User user = userRepository.findByPhone(sendOTPRequest.getMobile());
        if(user==null && sendOTPRequest.getEmail() != null && !"".equals(sendOTPRequest.getEmail().trim())){
            user = userRepository.findByEmail(sendOTPRequest.getEmail());
            if(user!=null){
                user.setPhone(sendOTPRequest.getMobile());
                userRepository.save(user);
            }
        }
        if(user==null && (sendOTPRequest.getEmail() == null || "".equals(sendOTPRequest.getEmail().trim()))){
            throw new UserDoesNotExistException("Your mobile number not registered, please login with your registered email.");
        }
        if(user==null){
            throw new UserDoesNotExistException("Sorry, we could not find your account, please contact admin.");
        }
        /*Message message = Message
                .creator(new PhoneNumber(sendOTPRequest.getCountryCode()+sendOTPRequest.getMobile()), // to
                        new PhoneNumber(environment.getProperty("TWILLIO_PHONE_NUMBER")), // from
                        "Your OTP is "+otpService.generateOTP(sendOTPRequest.getMobile())+"                " +
                                "                                  "+HASH_KEY)
                .create();*/
        String otpMessage = environment.getProperty("OTP_MESSAGE");
        if(otpMessage!=null){
            otpMessage = otpMessage.replace("*",otpService.generateOTP(sendOTPRequest.getMobile())+"");
        }else{
            otpMessage = "Your OTP is "+otpService.generateOTP(sendOTPRequest.getMobile());
        }
        sendTransmitSms(otpMessage+"                " +
                "                                  "+HASH_KEY,sendOTPRequest.getCountryCode()+sendOTPRequest.getMobile());
        ResponseEntity<Object> responseEntity = new ResponseEntity<>("OTP sent successfully.", HttpStatus.OK);
        return responseEntity;
    }

    public void sendTransmitSms(String message,String to) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("message", message);
        parameters.put("to", to);
        String encoding = Base64.getEncoder().encodeToString((TRANSMIT_SMS_API_KEY+":"+TRANSMIT_SMS_API_SECRET).getBytes());
        HttpGet httpGet = new HttpGet(TRANSMIT_SMS_URL+ParameterStringBuilder.getParamsString(parameters));
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        System.out.println("executing request " + httpGet.getRequestLine());
        HttpResponse response = HttpClientBuilder.create().build().execute(httpGet);
        if(response.getStatusLine().getStatusCode() != org.apache.http.HttpStatus.SC_OK){
            throw new RuntimeException("Unable to send sms"+response.getStatusLine().getStatusCode());
        }

       /* URL url = new URL("");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");


        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();
        con.setRequestProperty("Content-Type", "application/json");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();*/
    }
}
