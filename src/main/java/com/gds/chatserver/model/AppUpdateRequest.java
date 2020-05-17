package com.gds.chatserver.model;

import lombok.Data;

@Data
public class AppUpdateRequest {
    private String platform;
    private String version;
}
