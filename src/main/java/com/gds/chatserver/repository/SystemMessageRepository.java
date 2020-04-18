package com.gds.chatserver.repository;

import com.gds.chatserver.model.SystemMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemMessageRepository extends JpaRepository<SystemMessage,Long> {
    public List<SystemMessage> findByParent(SystemMessage parent);
}
