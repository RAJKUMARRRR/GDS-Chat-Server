package com.gds.chatserver.repository;

import com.gds.chatserver.model.Conversation;
import com.gds.chatserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    public List<Conversation> getAllByUserOneOrUserTwo(User userOne,User userTwo);
}
