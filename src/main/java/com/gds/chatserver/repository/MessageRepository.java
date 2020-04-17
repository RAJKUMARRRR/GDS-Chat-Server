package com.gds.chatserver.repository;

import com.gds.chatserver.model.Conversation;
import com.gds.chatserver.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    public List<Message> findMessagesByConversation(Conversation conversation);
}
