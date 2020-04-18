package com.gds.chatserver.model;

import com.gds.chatserver.enums.AccountStatus;
import com.gds.chatserver.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User extends Auditable{
    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private AccountStatus accountStatus;

    @Getter
    @Setter
    private Role role;

    @Getter
    @Setter
    private String profileImageUrl;

    @Getter
    @Setter
    private Long mailBoxNumber;

    public static User fromId(Long userId) {
        User user = new User();
        user.id = userId;
        return user;
    }

    public User(){}

    public User(String phone, String username) {
        this.phone = phone;
        this.username = username;
    }

    public  User(Long id){
        this.id = id;
    }
}
