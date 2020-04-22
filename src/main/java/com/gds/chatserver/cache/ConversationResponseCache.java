package com.gds.chatserver.cache;

import com.gds.chatserver.model.Conversation;
import com.gds.chatserver.model.ConversationResponse;
import com.gds.chatserver.model.User;
import com.gds.chatserver.repository.ConversationRepository;
import com.gds.chatserver.repository.UserRepository;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import java.util.*;

public class ConversationResponseCache {
    private static Map<Long, ConversationResponse> cache = new HashMap<>();
    public static void setItem(Long key, ConversationResponse conversationResponse){
        cache.put(key,conversationResponse);
    }
    public static ConversationResponse getItem(Long key){
        return cache.get(key);
    }
    public static void removeItem(Long key){
        cache.remove(key);
    }
    public static boolean isExist(Long key){
        return cache.containsKey(key);
    }
    public static List<ConversationResponse> getList(User user, ConversationRepository conversationRepository){
        Set<Map.Entry<Long,ConversationResponse>> entrySet = cache.entrySet();
        List<ConversationResponse> list = new ArrayList<>();
        for(Map.Entry<Long,ConversationResponse> entry: entrySet){
            ConversationResponse con = entry.getValue();
            if(user.getId() == con.getUserOne().getId() || user.getId() == con.getUserTwo().getId()){
                list.add(con);
            }
        }
        if(list.size()<=0){
            List<ConversationResponse> all = initializeCache(conversationRepository);
            for(ConversationResponse con:all){
                if(user.getId() == con.getUserOne().getId() || user.getId() == con.getUserTwo().getId()){
                    list.add(con);
                }
            }
        }
        return list;
    }
    public static List<ConversationResponse> initializeCache(ConversationRepository conversationRepository){
        List<Conversation> list =  conversationRepository.findAll();
        List<ConversationResponse> conversationResponses = new ArrayList<>();
        for(Conversation c: list){
            ConversationResponse con = new ConversationResponse();
            con.setCreatedAt(c.getCreatedAt());
            con.setUpdatedAt(c.getUpdatedAt());
            con.setId(c.getId());
            con.setUserOne(c.getUserOne());
            con.setUserTwo(c.getUserTwo());
            setItem(c.getId(),con);
            conversationResponses.add(con);
        }
        return conversationResponses;
    }
}
