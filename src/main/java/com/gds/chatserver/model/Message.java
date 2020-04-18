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
    @NotNull
    private String message;

    @Getter
    @Setter
    @NotNull
    private MessageStatus messageStatus;

    @Getter
    @Setter
    @NotNull
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
    @NotNull
    private User user;

    @JsonSetter("userId")
    public void setUser(Long userId) {
        user = User.fromId(userId);
    }


    @Setter
    @Getter
    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("conversationId")
    @NotNull
    private Conversation conversation;

    @JsonProperty("conversationId")
    public void setConversation(Long conversationId) {
        conversation = Conversation.fromId(conversationId);
    }

    @Getter
    @Setter
    private MessageSource messageSource;

    @Getter
    @Setter
    @OneToOne
    private SystemMessage systemMessage;

    public Message(){}

    public Message(String message, MessageStatus messageStatus, MessageType messageType,Media media,User user,Conversation conversation) {
        this.message = message;
        this.messageStatus = messageStatus;
        this.messageType = messageType;
        this.media = media;
        this.user = user;
        this.conversation = conversation;
    }
}
