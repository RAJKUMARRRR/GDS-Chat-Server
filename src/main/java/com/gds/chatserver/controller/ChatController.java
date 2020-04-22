package com.gds.chatserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gds.chatserver.cache.ConversationResponseCache;
import com.gds.chatserver.enums.MessageType;
import com.gds.chatserver.exceptions.ConversationNotFoundException;
import com.gds.chatserver.exceptions.MessageNotFoundException;
import com.gds.chatserver.model.*;
import com.gds.chatserver.repository.*;
import com.gds.chatserver.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/")
public class ChatController {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PushNotificationService pushNotificationService;

    @GetMapping("/messages/{id}")
    @CrossOrigin
    public Message getMessageById(@PathVariable("id") Long id){
        return messageRepository.findById(id).orElseThrow(()->new MessageNotFoundException("Message doesn't exist with id:"+id));
    }

    @PutMapping("/messages")
    @CrossOrigin
    public Message updateMessageById(@Validated @RequestBody Message message){
        return messageRepository.save(message);
    }

    @CrossOrigin
    @PostMapping(value = "/messages", consumes = APPLICATION_JSON_VALUE)
    public Message sendMessage(@Validated @RequestBody Message message) throws Exception {
        message = messageRepository.save(message);
        sendPushMessage(message);
        if(ConversationResponseCache.isExist(message.getConversation().getId())){
            ConversationResponse conversationResponse = ConversationResponseCache.getItem(message.getConversation().getId());
            conversationResponse.setLastUpdatedUserId(message.getUser().getId());
            conversationResponse.setUpdatedAt(new Date());
            ConversationResponseCache.setItem(conversationResponse.getId(),conversationResponse);
        }
        return message;
    }

    @Async
    void sendPushMessage(Message message) throws JsonProcessingException {
        Conversation conversation = conversationRepository.findById(message.getConversation().getId()).orElseThrow(()->new ConversationNotFoundException(("Conversation doesn't exist")));
        User toUser = conversation.getUserOne().getId() == message.getUser().getId() ?
                conversation.getUserTwo() : conversation.getUserOne();
        User fromUser = conversation.getUserOne().getId() == message.getUser().getId() ?
                conversation.getUserOne() : conversation.getUserTwo();
        if(toUser.getPushToken() == null || toUser.getPushToken().isEmpty()){
            return;
        }
        PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
        if(message.getMessageType()== MessageType.TEXT){
            pushNotificationRequest.setMessage(message.getMessage());
        }else{
            pushNotificationRequest.setMessage("Image");
        }
        pushNotificationRequest.setTitle("From:"+fromUser.getUsername());
        pushNotificationRequest.setToken(toUser.getPushToken());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String data = new ObjectMapper().setDateFormat(df).writeValueAsString(message);
        pushNotificationService.sendPushNotification(pushNotificationRequest,data);
    }


    @DeleteMapping("/messages/{id}")
    @CrossOrigin
    public void deleteMessage(@PathVariable("id") Long id){
        messageRepository.delete(getMessageById(id));
    }
}
