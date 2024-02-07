package com.self.flipcart.exceptionhandlers;

import com.self.flipcart.exceptions.*;
import com.self.flipcart.util.ErrorStructure;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class AuthExceptionHandlers {

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
        log.error(ex.getMessage()+" | "+"invalid email id");
        return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "invalid email id");
    }

    @ExceptionHandler(RegistrationSessionExpiredException.class)
    public ResponseEntity<Object> handleRegistrationSessionExpired(RegistrationSessionExpiredException ex){
        log.error(ex.getMessage()+" | "+"Took too long to register, the Registration session Expired");
        return structure(HttpStatus.EXPECTATION_FAILED, ex.getMessage(), "Took too long to register, the Registration session Expired. Try again");
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmail(DuplicateEmailException ex) {
        log.error(ex.getMessage()+" | "+"User already exists with the given email id");
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "User already exists with the given email id");
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<Object> handleOtpExpired(OtpExpiredException ex){
        log.error(ex.getMessage()+" | "+"The given OTP is expired");
        return structure(HttpStatus.EXPECTATION_FAILED, ex.getMessage(), "The given OTP is expired");
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundByIdException ex){
        log.error(ex.getMessage()+" | "+ "User not found with the given Id");
        return structure(HttpStatus.NOT_FOUND, ex.getMessage(), "User not found with the given Id");
    }

    @ExceptionHandler(IncorrectOTPException.class)
    public ResponseEntity<Object> handleIncorrectOTP(IncorrectOTPException ex){
        log.error(ex.getMessage()+" | "+"The given OTP is Incorrect");
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "The given OTP is Incorrect");
    }

    @ExceptionHandler(UserAlreadyExistsByEmailException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsByEmail(UserAlreadyExistsByEmailException ex){
        log.error(ex.getMessage()+" | "+"User Already exists with the given email");
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "User Already exists with the given email");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex){
        log.error(ex.getMessage()+" | "+"you are not allowed to access this resource");
        return structure(HttpStatus.FORBIDDEN, ex.getMessage(), "you are not allowed to access this resource");
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<Object> handleUserNotLoggedIn(UserNotLoggedInException ex){
        log.error(ex.getMessage()+" | "+"Login Expired or not logged in, please login again");
        return structure(HttpStatus.UNAUTHORIZED, ex.getMessage(), "Login Expired or not logged in, please login again");
    }

    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ResponseEntity<Object> handleUserAlreadyLoggedIn(UserAlreadyLoggedInException ex){
        log.error(ex.getMessage()+" | "+"You are already logged in, logout or clear cookie to login again");
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "You are already logged in, logout or clear cookie to login again");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(UsernameNotFoundException ex){
        log.error(ex.getMessage()+" | "+"Failed to find user");
        return structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Failed to find user");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex){
        log.error(ex.getMessage()+" | "+"The given username or password is incorrect");
        return structure(HttpStatus.FORBIDDEN, ex.getMessage(), "The given username or password is incorrect");
    }
}
