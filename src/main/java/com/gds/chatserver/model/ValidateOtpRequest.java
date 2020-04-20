package com.gds.chatserver.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Component
@Data
public class ValidateOtpRequest {
    @NotNull(message = "mobileNumber cannot be null")
    @NotBlank(message = "mobileNumber cannot be blank")
    @Size(min = 10,max = 10,message = "mobileNumber must be 10 digits")
    private String mobileNumber;


    @NotNull(message = "otp cannot be null")
    @NotBlank(message = "otp cannot be blank")
    @Size(min = 6,max = 6,message = "otp must be 6 digits")
    private String otp;
}
