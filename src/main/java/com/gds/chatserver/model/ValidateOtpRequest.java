package com.gds.chatserver.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ValidateOtpRequest {
    private String mobileNumber;
    private int otp;
}
