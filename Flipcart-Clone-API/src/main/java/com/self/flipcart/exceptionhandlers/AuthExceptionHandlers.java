package com.self.flipcart.exceptionhandlers;

import com.self.flipcart.exceptions.*;
import com.self.flipcart.util.ErrorStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(IncorrectOTPException.class)
    public ResponseEntity<Object> handleIncorrectOTP(IncorrectOTPException ex){
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "The given OTP is Incorrect");
    }

    @ExceptionHandler(UserAlreadyExistsByEmailException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsByEmail(UserAlreadyExistsByEmailException ex){
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "User Already exists with the given email");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex){
        return structure(HttpStatus.FORBIDDEN, ex.getMessage(), "you are not allowed to access this resource");
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<Object> handleUserNotLoggedIn(UserNotLoggedInException ex){
        return structure(HttpStatus.UNAUTHORIZED, ex.getMessage(), "Login Expired or not logged in, please login");
    }

    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ResponseEntity<Object> handleUserAlreadyLoggedIn(UserAlreadyLoggedInException ex){
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "You are already logged in, logout or clear cookie to login again");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(UsernameNotFoundException ex){
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Failed to find user");
    }
}
