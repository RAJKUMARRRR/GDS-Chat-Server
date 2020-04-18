package com.gds.chatserver.controller;

import com.gds.chatserver.exceptions.SystemMessageNotFoundException;
import com.gds.chatserver.model.SystemMessage;
import com.gds.chatserver.repository.SystemMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class SystemMessageController {
    @Autowired
    private SystemMessageRepository systemMessageRepository;

    @GetMapping("/system_message/{id}")
    @CrossOrigin
    public SystemMessage getSystemMessageById(@PathVariable Long id){
        return systemMessageRepository.findById(id).orElseThrow(()->new SystemMessageNotFoundException("SystemMessage not found with id:"+id));
    }

    @GetMapping("/system_message")
    @CrossOrigin
    public List<SystemMessage> getAllSystemMessages(@RequestParam("parentId") Long parentId){
        if(parentId == null){
            return systemMessageRepository.findAll();
        }
        return systemMessageRepository.findByParent(getSystemMessageById(parentId));
    }

    @PostMapping("/system_message")
    @CrossOrigin
    public SystemMessage createSystemMessage(@RequestBody SystemMessage systemMessage){
        return systemMessageRepository.save(systemMessage);
    }

    @PutMapping("/system_message")
    @CrossOrigin
    public SystemMessage updateSystemMessage(@RequestBody SystemMessage systemMessage){
        return systemMessageRepository.save(systemMessage);
    }

    @DeleteMapping("/system_message/{id}")
    public void deleteSystemMessage(@PathVariable Long id){
        SystemMessage systemMessage = getSystemMessageById(id);
        systemMessageRepository.delete(systemMessage);
    }
}