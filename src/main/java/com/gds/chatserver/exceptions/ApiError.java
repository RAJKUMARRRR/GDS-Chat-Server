package com.gds.chatserver.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ValidationError> validationErrors;

    public ApiError(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus){
        this();
        this.httpStatus = httpStatus;
    }

    public  ApiError(HttpStatus httpStatus,Throwable ex){
        this(httpStatus);
        this.message = "Unexpected Error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus httpStatus,String message,Throwable ex){
        this(httpStatus);
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
