package com.gds.chatserver.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gds.chatserver.model.ImageData;
import com.gds.chatserver.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dfpu0si4d",
            "api_key", "481164797455516",
            "api_secret", "aPzd_ZlL0lobXE3gYZDtY5Sni1c"));
    @CrossOrigin
    @RequestMapping(value = "/media/upload", method = POST)
    public Map greetingJson(@RequestBody ImageData imageData) throws IOException {
        Map uploadResult =  cloudinary.uploader().upload(imageData.getBase64(),ObjectUtils.emptyMap());
        return uploadResult;
    }
}
