package com.gds.chatserver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "system_messages")
@Data
public class SystemMessage extends Auditable{
    @NotNull(message = "message cannot be null")
    @NotBlank(message = "message cannot be blank")
    private String message;

    @ManyToOne
    @JsonBackReference
    private SystemMessage parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    private List<SystemMessage> children;
}
