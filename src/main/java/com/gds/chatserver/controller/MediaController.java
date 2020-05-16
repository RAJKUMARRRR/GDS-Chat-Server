package com.gds.chatserver.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gds.chatserver.exceptions.ValidationError;
import com.gds.chatserver.model.ImageData;
import com.gds.chatserver.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/")
public class MediaController {
    @Autowired
    private MediaRepository mediaRepository;
    private Cloudinary cloudinary;

    @Autowired
    MediaController(Environment environment){
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", environment.getProperty("CLOUDINARY_CLOUD_NAME"),
                "api_key", environment.getProperty("CLOUDINARY_API_KEY"),
                "api_secret", environment.getProperty("CLOUDINARY_API_SECRET")));
    }

    @CrossOrigin
    @RequestMapping(value = "/media/upload", method = POST)
    public Map greetingJson(@RequestBody ImageData imageData) throws IOException {
        if(cloudinary==null){
            throw new RuntimeException("Not able instatiate cloudinary");
        }
        Map uploadResult =  cloudinary.uploader().upload(imageData.getBase64(),ObjectUtils.emptyMap());
        return uploadResult;
    }
}
