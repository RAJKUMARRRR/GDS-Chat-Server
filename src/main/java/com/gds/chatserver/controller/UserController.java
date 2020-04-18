package com.gds.chatserver.controller;

import com.gds.chatserver.enums.MessageSource;
import com.gds.chatserver.enums.MessageStatus;
import com.gds.chatserver.enums.MessageType;
import com.gds.chatserver.enums.Role;
import com.gds.chatserver.exceptions.UserDoesNotExistException;
import com.gds.chatserver.model.Conversation;
import com.gds.chatserver.model.Message;
import com.gds.chatserver.model.User;
import com.gds.chatserver.repository.ConversationRepository;
import com.gds.chatserver.repository.MessageRepository;
import com.gds.chatserver.repository.UserRepository;
import com.gds.chatserver.service.UserDetailsServiceImpl;
import com.gds.chatserver.utils.ModelUtils;
import org.hibernate.QueryParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @CrossOrigin
    @GetMapping("/users/")
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
    public User getLoogedInUser(){
        return userDetailsService.getLoggedInUser();
    }

    @CrossOrigin
    @PostMapping("/users/")
    public User createUser(@RequestBody User user){
        if(user.getRole() == Role.ADMIN && userDetailsService.getLoggedInUser().getRole() != Role.ADMIN){
            throw new AccessDeniedException("You are not allowed to set role ADMIN");
        }
        userRepository.save(user);
        if(user.getRole() != Role.ADMIN){
            Conversation conversation = new Conversation();
            conversation.setUserOne(userRepository.findByRole(Role.ADMIN).getId());
            conversation.setUserTwo(user.getId());
            conversationRepository.save(conversation);
            messageRepository.saveAll(ModelUtils.getGreetingMessages(conversation,user));
        }
        return user;
    }

    @CrossOrigin
    @PutMapping("/users/")
    public User updateUser(@RequestBody User user){
        checkAccess(user.getId());
        userRepository.save(user);
        return user;
    }


    @DeleteMapping("/users/{id}")
    @CrossOrigin
    public  void deleteUser(@PathVariable("id") Long id){
        userRepository.delete(getUserById(id));
    }

    @CrossOrigin
    @GetMapping("/users/conversations")
    public List<Conversation> getConversationsByUser(@RequestParam(name = "userId",required = true) Long userId) throws UserDoesNotExistException {
        if(userId==null){
            throw new QueryParameterException("Missing query parameter 'userId'");
        }
        User loggedInUser = userDetailsService.getLoggedInUser();
        if(loggedInUser.getId() != userId){
            throw new AccessDeniedException("You are not authorized to view conversations of userId:"+userId);
        }
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent())
            return conversationRepository.getAllByUserOneOrUserTwo(user.get(),user.get());
        else
            throw new UserDoesNotExistException("User doesn't exist with id:"+userId);
    }


    private void checkAccess(long id){
        if(id != userDetailsService.getLoggedInUser().getId()){
            throw  new AccessDeniedException("You are not authorized do this operation");
        }
    }
}
