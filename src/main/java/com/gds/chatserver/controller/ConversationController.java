package com.gds.chatserver.controller;

import com.gds.chatserver.cache.ConversationResponseCache;
import com.gds.chatserver.enums.Role;
import com.gds.chatserver.exceptions.ConversationNotFoundException;
import com.gds.chatserver.exceptions.UserDoesNotExistException;
import com.gds.chatserver.model.Conversation;
import com.gds.chatserver.model.ConversationResponse;
import com.gds.chatserver.model.Message;
import com.gds.chatserver.model.User;
import com.gds.chatserver.repository.ConversationRepository;
import com.gds.chatserver.repository.MessageRepository;
import com.gds.chatserver.repository.UserRepository;
import com.gds.chatserver.service.UserDetailsServiceImpl;
import org.hibernate.QueryParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ConversationController {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserController userController;

    @CrossOrigin
    @GetMapping("/conversations")
    public List<Conversation> getAllConversations(){
        if(userDetailsService.getLoggedInUser().getRole()!= Role.ADMIN){
            throw new AccessDeniedException("Your are not allowed for this operation");
        }
        return conversationRepository.findAll();
    }

    @CrossOrigin
    @GetMapping("/conversations/{id}")
    public Conversation getConversationById(@PathVariable("id") Long id){
        return conversationRepository.findById(id).orElseThrow(()->new ConversationNotFoundException("Conversation doesn't exist with id:"+id));
    }


    @CrossOrigin
    @PostMapping("/conversations")
    public Conversation createConversation(@Validated @RequestBody Conversation conversation){
        conversation = conversationRepository.save(conversation);
        conversation.setUserOneObject(userController.getUserById(conversation.getUserOne().getId()));
        conversation.setUserTwoObject(userController.getUserById(conversation.getUserTwo().getId()));
        ConversationResponseCache.setItem(conversation.getId(),new ConversationResponse(conversation));
        return conversation;
    }

    @CrossOrigin
    @DeleteMapping("/conversations/{id}")
    public void deletedConversation(@PathVariable("id") Long id){
        Conversation conversation = getConversationById(id);
        List<Message> messages = messageRepository.findMessagesByConversationOrderByCreatedAt(conversation);
        messageRepository.deleteInBatch(messages);
        conversationRepository.delete(conversation);
        ConversationResponseCache.setItem(conversation.getId(),new ConversationResponse(conversation));
        ConversationResponseCache.removeItem(id);
    }

    @CrossOrigin
    @GetMapping("/conversations/messages")
    public List<Message> getChat(@RequestParam(name = "conversationId") Long conversationId) {
        if(conversationId == null){
            throw new QueryParameterException("Missing query parameter 'conversationId'");
        }
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(()->new ConversationNotFoundException("No conversation found with conversationId:"+conversationId));
        User loggedInUser = userDetailsService.getLoggedInUser();
        if(loggedInUser.getId() != conversation.getUserOne().getId() && loggedInUser.getId() != conversation.getUserTwo().getId()){
            throw new AccessDeniedException("You are not authorized to view this conversation");
        }
        return messageRepository.findMessagesByConversationOrderByCreatedAt(conversationRepository.findById(conversationId).orElseThrow(()->new ConversationNotFoundException("Conversation does not exit with id:"+conversationId)));
    }

    @GetMapping("/conversations/mark_read/{id}")
    @CrossOrigin
    public void readConversation(@PathVariable("id") Long id){
        if(ConversationResponseCache.isExist(id)){
            ConversationResponse conversationResponse = ConversationResponseCache.getItem(id);
            if(conversationResponse.getLastUpdatedUserId() != userDetailsService.getLoggedInUser().getId()){
                conversationResponse.setLastUpdatedUserId(null);
                conversationResponse.setUnreadCount(0);
            }
        }
    }
}
