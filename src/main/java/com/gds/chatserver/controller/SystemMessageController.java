package com.gds.chatserver.controller;

import com.gds.chatserver.exceptions.SystemMessageNotFoundException;
import com.gds.chatserver.model.SystemMessage;
import com.gds.chatserver.repository.SystemMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
public class SystemMessageController {
    @Autowired
    private SystemMessageRepository systemMessageRepository;

    @GetMapping("/system_messages/admin")
    @CrossOrigin
    public List<SystemMessage> getAdminMessages(){
        return systemMessageRepository.findAllByParentIsNull();
    }

    @GetMapping("/system_messages/{id}")
    @CrossOrigin
    public SystemMessage getSystemMessageById(@PathVariable Long id){
        return systemMessageRepository.findById(id).orElseThrow(()->new SystemMessageNotFoundException("SystemMessage not found with id:"+id));
    }

    @GetMapping("/system_messages")
    @CrossOrigin
    public List<SystemMessage> getAllSystemMessages(@RequestParam("parentId") Long parentId){
        if(parentId == null){
            return systemMessageRepository.findAll();
        }
        return systemMessageRepository.findByParent(getSystemMessageById(parentId));
    }

    @PostMapping("/system_messages")
    @CrossOrigin
    public SystemMessage createSystemMessage(@Validated @RequestBody SystemMessage systemMessage){
        return systemMessageRepository.save(systemMessage);
    }

    @PutMapping("/system_messages")
    @CrossOrigin
    public SystemMessage updateSystemMessage(@Validated @RequestBody SystemMessage systemMessage){
        return systemMessageRepository.save(systemMessage);
    }

    @DeleteMapping("/system_messages/{id}")
    public void deleteSystemMessage(@PathVariable Long id){
        SystemMessage systemMessage = getSystemMessageById(id);
        systemMessageRepository.delete(systemMessage);
    }
}
