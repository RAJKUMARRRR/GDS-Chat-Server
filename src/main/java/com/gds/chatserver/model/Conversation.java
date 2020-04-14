package com.gds.chatserver.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "conversation")
public class Conversation  extends Auditable{
    @Getter
    @Setter
    @OneToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonSetter("userOneId")
    private User userOne;

    @JsonSetter("userOneId")
    public void setUserOne(Long userId) {
        userOne = User.fromId(userId);
    }



    @Getter
    @Setter
    @OneToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonSetter("userTwoId")
    private User userTwo;

    @JsonSetter("userTwoId")
    public void setUserTwo(Long userId) {
        userTwo = User.fromId(userId);
    }

    public static Conversation fromId(Long conversationId) {
        Conversation conversation = new Conversation();
        conversation.id = conversationId;
        return conversation;
    }

    public  Conversation(){}

    public Conversation(User userOne, User userTwo) {
        this.userOne = userOne;
        this.userTwo = userTwo;
    }

    public Conversation(Long id){
        this.id = id;
    }
}
