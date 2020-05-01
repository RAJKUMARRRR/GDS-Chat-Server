package com.gds.chatserver.model;

import lombok.Data;

import javax.persistence.Entity;

@Entity(name="country_codes")
@Data
public class CountryCode extends Auditable {
    private String mobileCode;
    private String countryCodeTwo;
    private String countryCodeThree;
    private String title;
}
