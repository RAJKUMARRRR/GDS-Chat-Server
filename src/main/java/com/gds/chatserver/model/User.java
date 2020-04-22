package com.gds.chatserver.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gds.chatserver.enums.AccountStatus;
import com.gds.chatserver.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User extends Auditable{
    @Getter
    @Setter
    @NotNull(message = "phone cannot be null")
    @NotBlank(message = "phone cannot be blank")
    @Size(min = 10,max = 10,message = "phone number must be 10 digits")
    private String phone;

    @Getter
    @Setter
    @NotNull(message = "username cannot be null")
    @NotBlank(message = "username cannot be blank")
    @Size(min = 1,max = 30,message = "username cannot be more than 30 characters")
    private String username;

    @Getter
    @Setter
    @Email(message = "email format is not correct")
    private String email;

    @Getter
    @Setter
    @NotNull(message = "accountStatus cannot be null, possible values(ACTIVE,BLOCKED,DELETED)")
    private AccountStatus accountStatus;

    @Getter
    @Setter
    @NotNull(message = "role cannot be null, possible values(ADMIN,CUSTOMER)")
    private Role role;

    @Getter
    @Setter
    private String profileImageUrl;

    @Getter
    @Setter
    @NotNull(message = "mailBoxNumber cannot be null")
    @NotBlank(message = "mailBoxNumber cannot be blank")
    private String mailBoxNumber;

    @Getter
    @Setter
    private String pushToken;

    @Transient
    @JsonInclude
    private List<ConversationResponse> conversations;

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

    public  User(User user){
        this.id = user.getId();
        this.setCreatedAt(user.getCreatedAt());
        this.setUpdatedAt(user.getUpdatedAt());
        this.phone = user.getPhone();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.accountStatus = user.getAccountStatus();
        this.role = user.getRole();
        this.profileImageUrl = user.getProfileImageUrl();
        this.mailBoxNumber = user.getMailBoxNumber();
        this.pushToken = user.getPushToken();
        this.conversations = user.getConversations();
    }
}
