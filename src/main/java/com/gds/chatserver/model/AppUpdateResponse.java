package com.gds.chatserver.model;

import lombok.Data;

@Data
public class AppUpdateResponse {
    private boolean updateAvailable;
    private boolean mandatoryUpdate;
}
