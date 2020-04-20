package com.gds.chatserver.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    ValidationError(){}

    ValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}