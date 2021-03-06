package com.gds.chatserver.repository;

import com.gds.chatserver.enums.Role;
import com.gds.chatserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findByPhone(String phone);
    public User findByRole(Role role);
    public User findByEmail(String email);
}
