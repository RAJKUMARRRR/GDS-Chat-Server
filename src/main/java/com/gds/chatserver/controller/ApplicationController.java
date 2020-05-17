package com.gds.chatserver.controller;


import com.gds.chatserver.model.AppInfo;
import com.gds.chatserver.model.AppUpdateRequest;
import com.gds.chatserver.model.AppUpdateResponse;
import com.gds.chatserver.repository.AppInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ApplicationController {
    @Autowired
    private AppInfoRepository appInfoRepository;

    @PostMapping("/checkAppUpdate")
    public AppUpdateResponse checkAppUpdate(@RequestBody AppUpdateRequest appUpdateRequest){
        List<AppInfo> appInfos = appInfoRepository.getAllByPlatform(appUpdateRequest.getPlatform());
        AppUpdateResponse appUpdateResponse = new AppUpdateResponse();
        appUpdateResponse.setMandatoryUpdate(false);
        appUpdateResponse.setUpdateAvailable(false);
        if(appInfos.size()>0){
            String serverVersion = appInfos.get(0).getVersion();
            String clientVersion = appUpdateRequest.getVersion();
            if(serverVersion.compareTo(clientVersion)>=1){
                appUpdateResponse.setUpdateAvailable(true);
                appUpdateResponse.setMandatoryUpdate(appInfos.get(0).getMandatoryUpdate() == 't');
            }
        }
        return appUpdateResponse;
    }
}
