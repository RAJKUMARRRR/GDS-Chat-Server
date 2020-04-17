package com.gds.chatserver.controller;

import com.gds.chatserver.repository.ConversationRepository;
import com.gds.chatserver.repository.MessageRepository;
import com.gds.chatserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DevController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @GetMapping("/populate")
    public String initDb(){
        /*mediaRepository.deleteAll();
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        userRepository.deleteAll();

        Media media = new Media(MediaType.IMAGE,"test file","https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcT-2GHtOA0uT2puqFFFjDDpCiB9i6C0aAM3DS_MTHBPGhnc98IO&usqp=CAU");
        mediaRepository.save(media);

        User userOne = new User("8501096987","root");
        userRepository.save(userOne);
        User userTwo = new User("8919468996","root");
        userRepository.save(userTwo);
        Conversation conversation = new Conversation(userOne,userTwo);
        conversationRepository.save(conversation);
        Conversation conversation1 = new Conversation(userOne,userTwo);
        conversationRepository.save(conversation1);
        Conversation conversation2 = new Conversation(userOne,userTwo);
        conversationRepository.save(conversation2);*/
       /* Message message = new Message("Test Message", userOne,conversation, MessageStatus.SEND, MessageType.TEXT,null);
        messageRepository.save(message);
        Message message1 = new Message("Test Message", userOne,conversation, MessageStatus.SEND, MessageType.TEXT,null);
        messageRepository.save(message1);
        Message message2 = new Message("Test Message", userTwo,conversation, MessageStatus.SEND, MessageType.MEDIA,null);
        messageRepository.save(message2);
        Message message3 = new Message("Test Message", userOne,conversation, MessageStatus.SEND, MessageType.TEXT,null);
        messageRepository.save(message3);
        Message message4 = new Message("Test Message", userOne,conversation, MessageStatus.SEND, MessageType.TEXT,null);
        messageRepository.save(message4);
        Message message5 = new Message("Test Message", userTwo,conversation, MessageStatus.SEND, MessageType.MEDIA,null);
        messageRepository.save(message5);*/
        return  "Success";
    }
}
