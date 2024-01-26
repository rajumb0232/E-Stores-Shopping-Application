package com.self.flipcart.exceptionhandlers;

import com.self.flipcart.exceptions.DuplicateEmailException;
import com.self.flipcart.exceptions.EmailNotFoundException;
import com.self.flipcart.exceptions.OtpExpiredException;
import com.self.flipcart.exceptions.UserNotFoundByIdException;
import com.self.flipcart.util.ErrorStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AuthExceptionHandlers {

    @Autowired
    private ErrorStructure<String> structure;

    private ResponseEntity<Object> structure(HttpStatus status, String message, String rootCause) {
        return new ResponseEntity<>(
                structure.setStatus(status.value())
                        .setMessage(message)
                        .setRootCause(rootCause)
                , status);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> handleEmailNotFound(EmailNotFoundException ex) {
        return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "invalid email id");
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmail(DuplicateEmailException ex) {
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "User already exists with the given email id.");
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<Object> handleOtpExpired(OtpExpiredException ex){
        return structure(HttpStatus.EXPECTATION_FAILED, ex.getMessage(), "The given OTP is expired");
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundByIdException ex){
        return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "User not found with the given Id");
    }
}
