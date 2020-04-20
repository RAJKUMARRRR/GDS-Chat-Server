package com.gds.chatserver.exceptions;

import com.twilio.exception.ApiException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({UserDoesNotExistException.class})
    protected ResponseEntity<Object> handleUserDoesNotExistException(UserDoesNotExistException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND,ex.getMessage(),ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({ConversationNotFoundException.class})
    protected ResponseEntity<Object> handleConversationNotFoundException(ConversationNotFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND,ex.getMessage(),ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({MessageNotFoundException.class})
    protected ResponseEntity<Object> handleMessageNotFoundException(MessageNotFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND,ex.getMessage(),ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({InvalidOTPException.class})
    protected ResponseEntity<Object> handleInvalidOTPException(InvalidOTPException ex) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED,ex.getMessage(),ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED,ex.getMessage(),ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({ResponseStatusException.class})
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED,ex.getMessage(),ex);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({ApiException.class})
    protected ResponseEntity<Object> handleApiException(ApiException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST,ex.getMessage(),ex);
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,"Invalid Request",ex);
        List<ValidationError> validationErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            ValidationError validationError = new ValidationError();
            validationError.setField(fieldName);
            validationError.setMessage(errorMessage);
            validationError.setRejectedValue(((FieldError) error).getRejectedValue());
            validationError.setObject(error.getObjectName());
            validationErrors.add(validationError);
        });
        apiError.setValidationErrors(validationErrors);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
