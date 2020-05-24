package com.gds.chatserver.exceptions;

import lombok.Data;

@Data
public class DuplicateRowException extends RuntimeException {
    private String column;
    public DuplicateRowException(String column, String message){
        super(message);
        this.column = column;
    }
}
