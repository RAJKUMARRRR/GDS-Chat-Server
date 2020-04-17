package com.gds.chatserver.controller;

import com.gds.chatserver.exceptions.MessageNotFoundException;
import com.gds.chatserver.model.*;
import com.gds.chatserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/")
public class ChatController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/messages/{id}")
    @CrossOrigin
    public Message getMessageById(@PathVariable("id") Long id){
        return messageRepository.findById(id).orElseThrow(()->new MessageNotFoundException("Message doesn't exist with id:"+id));
    }

    @PutMapping("/messages")
    @CrossOrigin
    public Message updateMessageById(@RequestBody Message message){
        return messageRepository.save(message);
    }

    @CrossOrigin
    @PostMapping(value = "/messages", consumes = APPLICATION_JSON_VALUE)
    public Message sendMessage(@RequestBody Message message){
        return messageRepository.save(message);
    }
}
