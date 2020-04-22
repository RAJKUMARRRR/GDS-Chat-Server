package com.gds.chatserver.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class ConversationResponse {
    private Long id;
    private User userOne;
    private User userTwo;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
    private Long lastUpdatedUserId;
    private int unreadCount;
    public ConversationResponse(){}
    public ConversationResponse(Conversation conversation){
        this.setCreatedAt(conversation.getCreatedAt());
        this.setUpdatedAt(conversation.getUpdatedAt());
        this.setId(conversation.getId());
        this.setUserOne(conversation.getUserOne());
        this.setUserTwo(conversation.getUserTwo());
        this.setLastUpdatedUserId(null);
        this.setUnreadCount(0);
    }
}
