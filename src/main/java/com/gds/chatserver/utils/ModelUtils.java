package com.gds.chatserver.utils;

import com.gds.chatserver.enums.MessageSource;
import com.gds.chatserver.enums.MessageStatus;
import com.gds.chatserver.enums.MessageType;
import com.gds.chatserver.enums.Role;
import com.gds.chatserver.model.Conversation;
import com.gds.chatserver.model.Message;
import com.gds.chatserver.model.User;
import com.gds.chatserver.repository.ConversationRepository;
import com.gds.chatserver.repository.MessageRepository;
import com.gds.chatserver.repository.UserRepository;
import com.gds.chatserver.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class ModelUtils {
    @Autowired
    private static UserRepository userRepository;

    @Autowired
    private static UserDetailsServiceImpl userDetailsService;

    @Autowired
    private static ConversationRepository conversationRepository;


















































    @Autowired
    private static MessageRepository messageRepository;

    public static List<Message> getGreetingMessages(Conversation conversation, User user,User admin){
        Message messageOne = new Message();
        messageOne.setConversation(conversation.getId());
        messageOne.setUser(admin.getId());
        messageOne.setMessage("Hi "+user.getUsername());
        messageOne.setMessageSource(MessageSource.USER);
        messageOne.setMessageType(MessageType.TEXT);
        messageOne.setMessageStatus(MessageStatus.SEND);
        Message messageTwo = new Message();
        messageTwo.setConversation(conversation.getId());
        messageTwo.setUser(admin.getId());
        messageTwo.setMessage("Welcome to GatewayDigitalServices !!");
        messageTwo.setMessageSource(MessageSource.USER);
        messageTwo.setMessageType(MessageType.TEXT);
        messageTwo.setMessageStatus(MessageStatus.SEND);
        List<Message> list = Arrays.asList(messageOne,messageTwo);
        return list;
    }
}
