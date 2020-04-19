package com.gds.chatserver.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class ConversationResponse {
    private Long id;
    private User user;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
}
