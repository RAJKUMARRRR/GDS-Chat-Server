package com.gds.chatserver.model;

import com.gds.chatserver.enums.MediaType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "media")
public class Media extends Auditable {
    @Getter
    @Setter
    @NotNull(message = "mediaType cannot be null, possible values(IMAGE,VIDEO,FILE)")
    private MediaType mediaType;

    @Getter
    @Setter
    @NotNull(message = "finamename cannot be null")
    @NotBlank(message = "filename cannot be blank")
    private String filename;

    @Getter
    @Setter
    @NotNull(message = "sourceUrl cannot be null")
    @NotBlank(message = "sourceUrl cannot be blank")
    private String sourceUrl;

    @Getter
    @Setter
    private Short deleted=0;

    public  Media(){}

    public Media(MediaType mediaType, String filename, String sourceUrl) {
        this.mediaType = mediaType;
        this.filename = filename;
        this.sourceUrl = sourceUrl;
    }
}
