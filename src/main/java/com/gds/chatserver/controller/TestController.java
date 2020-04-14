package com.gds.chatserver.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gds.chatserver.enums.MediaType;
import com.gds.chatserver.exceptions.UserDoesNotExist;
import com.gds.chatserver.model.*;
import com.gds.chatserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MediaRepository mediaRepository;

   // Cloudinary cloudinary = Singleton.getCloudinary();
   Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
           "cloud_name", "dfpu0si4d",
           "api_key", "481164797455516",
           "api_secret", "aPzd_ZlL0lobXE3gYZDtY5Sni1c"));

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

    @CrossOrigin
    @GetMapping("/chat")
    public List<Message> getChat(@RequestParam(name = "conversationId") Long conversationId) {
        /*return messageRepository.findMessagesByConversation(conversationRepository.findById(conversationId).orElseThrow());*/
        return messageRepository.findAll();
    }

    @CrossOrigin
    @PostMapping(value = "/chat", consumes = APPLICATION_JSON_VALUE)
    public Message sendMessage(@RequestBody Message message){
        return messageRepository.save(message);
    }

    @CrossOrigin
    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @CrossOrigin
    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        userRepository.save(user);
        return user;
    }

    public List<Conversation> getAllConversations(){
        return conversationRepository.findAll();
    }

    @CrossOrigin
    @GetMapping("/conversations")
    public List<Conversation> getConversationsByUser(@RequestParam(name = "userId",required = false) Long userId) throws UserDoesNotExist {
        //if(userId==null){
            return getAllConversations();
        //}
        /*Optional<User> user = userRepository.findById(userId);
        if(user.isPresent())
        return conversationRepository.getAllByUserOneOrUserTwo(user.get(),user.get());
        else
            throw new UserDoesNotExist("User doesn't exist with id:"+userId);*/
    }

    @CrossOrigin
    @PostMapping("/conversations")
    public Conversation createConversation(@RequestBody Conversation conversation){
        return conversationRepository.save(conversation);
    }

    @CrossOrigin
    @RequestMapping(value = "/upload", method = POST)
    public Map greetingJson(@RequestBody ImageData imageData) throws IOException {
        /*StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(file.getBytes(), false)));*/
        //Map uploadResult =  cloudinary.uploader().upload("https://images.unsplash.com/photo-1522204523234-8729aa6e3d5f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80",ObjectUtils.emptyMap());
        Map uploadResult =  cloudinary.uploader().upload(imageData.getBase64(),ObjectUtils.emptyMap());
        return uploadResult;
    }
}
