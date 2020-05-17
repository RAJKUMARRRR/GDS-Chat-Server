package com.gds.chatserver.model;


import com.fasterxml.jackson.annotation.*;
import com.gds.chatserver.controller.UserController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "conversation")
public class Conversation  extends Auditable{
    @Getter
    @Setter
    @OneToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonSetter("userOneId")
    @NotNull(message = "userOneId cannot be null")
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
    @NotNull(message = "userTwoId cannot be null")
    private User userTwo;

    @JsonSetter("userTwoId")
    public void setUserTwo(Long userId) {
        userTwo = User.fromId(userId);
    }

    public void setUserOneObject(User user){
        userOne = user;
    }
    public void setUserTwoObject(User user){
        userTwo = user;
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
