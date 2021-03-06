package com.gds.chatserver.controller;

import com.gds.chatserver.cache.ConversationResponseCache;
import com.gds.chatserver.enums.MessageSource;
import com.gds.chatserver.enums.Role;
import com.gds.chatserver.exceptions.DuplicateRowException;
import com.gds.chatserver.exceptions.UserDoesNotExistException;
import com.gds.chatserver.model.*;
import com.gds.chatserver.repository.ConversationRepository;
import com.gds.chatserver.repository.MediaRepository;
import com.gds.chatserver.repository.MessageRepository;
import com.gds.chatserver.repository.UserRepository;
import com.gds.chatserver.service.UserDetailsServiceImpl;
import com.gds.chatserver.utils.ModelUtils;
import org.hibernate.QueryParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.core.env.Environment;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationController conversationController;

    @Autowired
    private Environment environment;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaController mediaController;


    @CrossOrigin
    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    @CrossOrigin
    public User getUserById(@PathVariable("id") Long id){
        checkAccess(id);
        return userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User doesn't exist with id:"+id));
    }

    @GetMapping("/users/profile")
    @CrossOrigin
    public User getLoogedInUser() throws CloneNotSupportedException {
        User user = userDetailsService.getLoggedInUser();
        /*List<Conversation> list =  conversationRepository.getAllByUserOneOrUserTwo(user,user);
        List<ConversationResponse> conversationResponses = new ArrayList<>();
        for(Conversation c: list){
            ConversationResponse con = new ConversationResponse();
            con.setCreatedAt(c.getCreatedAt());
            con.setUpdatedAt(c.getUpdatedAt());
            con.setId(c.getId());
            con.setUserOne(c.getUserOne());
            con.setUserTwo(c.getUserTwo());
            conversationResponses.add(con);
        }
        user.setConversations(conversationResponses);*/
        User clonedUser = new User(user);
        clonedUser.setConversations(ConversationResponseCache.getList(user,conversationRepository));
        Collections.sort(clonedUser.getConversations(),(a,b)->{
            return b.getUpdatedAt().compareTo(a.getUpdatedAt());
        });
        return clonedUser;
    }

    @CrossOrigin
    @PostMapping("/users")
    public User createUser(@Validated @RequestBody User user){
        if(user.getRole() == Role.ADMIN && userDetailsService.getLoggedInUser().getRole() != Role.ADMIN){
            throw new AccessDeniedException("You are not allowed to set role ADMIN");
        }
        if(user.getPhone()!=null&&!checkUniqueKeyConstraint(user.getPhone(),"phone")){
            throw new DuplicateRowException("phone","Mobile number already registered");
        }
        if(user.getEmail()!=null&&!checkUniqueKeyConstraint(user.getEmail(),"email")){
            throw new DuplicateRowException("email","Email already registered");
        }
        userRepository.save(user);
        if(user.getRole() != Role.ADMIN){
            Conversation conversation = new Conversation();
            User admin = userRepository.findByRole(Role.ADMIN);
            conversation.setUserOne(admin.getId());
            conversation.setUserTwo(user.getId());
            //conversationRepository.save(conversation);
            conversationController.createConversation(conversation);
            messageRepository.saveAll(ModelUtils.getGreetingMessages(conversation,user,admin,environment.getProperty("WELCOME_MESSAGE")));
        }
        return user;
    }

    public boolean checkUniqueKeyConstraint(String value,String column){
        switch (column){
            case "phone":
                if(userRepository.findByPhone(value)!=null){
                    return false;
                }
                break;
            case "email":
                if(userRepository.findByEmail(value)!=null){
                    return false;
                }
                break;
        }
        return true;
    }

    @CrossOrigin
    @PutMapping("/users")
    public User updateUser(@Validated @RequestBody User user) throws UserDoesNotExistException {
        if(user.getId()==null){
            throw new UserDoesNotExistException("User does not exist with id null");
        }
        checkAccess(user.getId());
        userRepository.save(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Long id) throws IOException {
        User user = getUserById(id);
        List<Conversation> conversationList = conversationRepository.getAllByUserOneOrUserTwo(user,user);
        for(Conversation conversation:conversationList){
            List<Message> messageList = messageRepository.findMessagesByConversationOrderByCreatedAt(conversation);
            for(Message message:messageList){
                if(message.getMedia()!=null){
                    mediaController.deleteImage(message.getMedia().getFilename());
                    mediaRepository.delete(message.getMedia());
                }
                if(message.getMessageSource()==null){
                    message.setMessageSource(MessageSource.USER);
                }
            }
            messageRepository.deleteAll(messageList);
        }
        conversationRepository.deleteAll(conversationList);
        userRepository.delete(user);
    }

    @CrossOrigin
    @GetMapping("/users/conversations")
    public List<ConversationResponse> getConversationsByUser() throws UserDoesNotExistException {
        User user = userDetailsService.getLoggedInUser();
        return ConversationResponseCache.getList(user,conversationRepository);
    }


    private void checkAccess(long id){
        if(userDetailsService.getLoggedInUser().getRole() == Role.ADMIN){
            return;
        }
        if(id != userDetailsService.getLoggedInUser().getId()){
            throw  new AccessDeniedException("You are not authorized do this operation");
        }
    }
}
