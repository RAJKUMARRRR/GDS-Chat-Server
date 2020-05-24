package com.gds.chatserver.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SendOTPRequest {
    private String email;
    private String mobile;
    private String countryCode;
}
