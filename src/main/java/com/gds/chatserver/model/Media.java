package com.gds.chatserver.model;

import com.gds.chatserver.enums.MediaType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity(name = "media")
public class Media extends Auditable {
    @Getter
    @Setter
    private MediaType mediaType;

    @Getter
    @Setter
    private String filename;

    @Getter
    @Setter
    private String sourceUrl;

    public  Media(){}

    public Media(MediaType mediaType, String filename, String sourceUrl) {
        this.mediaType = mediaType;
        this.filename = filename;
        this.sourceUrl = sourceUrl;
    }
}
