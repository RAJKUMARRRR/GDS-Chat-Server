package com.gds.chatserver.model;

import com.fasterxml.jackson.annotation.*;
import com.gds.chatserver.enums.MessageSource;
import com.gds.chatserver.enums.MessageStatus;
import com.gds.chatserver.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "message")
public class Message extends Auditable{
    @Getter
    @Setter
    @NotNull(message = "message cannot be null")
    private String message;

    @Getter
    @Setter
    @NotNull(message = "messageStatus cannot be null, possible values(SEND,RECEIVED,READ,FAILED,DELETED,PENDING)")
    private MessageStatus messageStatus;

    @Getter
    @Setter
    @NotNull(message = "messageType cannot be null, possible values(TEXT,MEDIA)")
    private MessageType messageType;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    private Media media;

    @Getter
    @Setter
    @OneToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonSetter("userId")
    @NotNull(message = "userId cannot be null")
    private User user;

    @JsonSetter("userId")
    public void setUser(Long userId) {
        user = User.fromId(userId);
    }


    @Setter
    @Getter
    @ManyToOne(cascade = CascadeType.DETACH)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("conversationId")
    @NotNull(message = "conversationId cannot be null")
    private Conversation conversation;

    @JsonProperty("conversationId")
    public void setConversation(Long conversationId) {
        conversation = Conversation.fromId(conversationId);
    }

    @Getter
    @Setter
    @NotNull(message = "messageSource cannot be null, possiblr values(SYSTEM,USER)")
    private MessageSource messageSource = MessageSource.USER;

    @Getter
    @Setter
    @OneToOne
    private SystemMessage systemMessage;

    public Message(){}
}
