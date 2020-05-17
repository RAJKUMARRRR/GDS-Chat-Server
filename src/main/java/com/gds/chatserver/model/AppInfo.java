package com.gds.chatserver.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="appinfo")
@Data
public class AppInfo extends Auditable{
    private String appName;
    private String description;
    private String platform;
    private String version;
    private Character mandatoryUpdate;
}
