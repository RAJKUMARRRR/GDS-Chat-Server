package com.gds.chatserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gds.chatserver.model.CountryCode;
import com.gds.chatserver.repository.CountryCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/")
public class ReferenceDataController {

    @Autowired
    private CountryCodeRepository countryCodeRepository;

    @GetMapping("/referenceData/countryCodes")
    private List<CountryCode> getCountryCodes(){
        return countryCodeRepository.findAllByOrderByTitleAsc();
    }

    @GetMapping("/referenceData/countryCodes/import")
    private void importCountryCodes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<CountryCode>> typeReference = new TypeReference<List<CountryCode>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/referencedata/countryCodes2.json");
        List<CountryCode> countryCodes = mapper.readValue(inputStream,typeReference);
        countryCodeRepository.deleteAll();
        countryCodeRepository.saveAll(countryCodes);
        System.out.println("CountryCodes Saved!");
    }
}
