package com.gds.chatserver.repository;

import com.gds.chatserver.model.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppInfoRepository extends JpaRepository<AppInfo,Long> {
    public List<AppInfo> getAllByPlatform(String platform);
}
