package com.self.flipcart.exceptionhandlers;

import com.self.flipcart.util.ErrorStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponse {
    private ErrorStructure<Object> structure;

    public ResponseEntity<Object> structure(HttpStatus status, String message, Object rootCause) {
        return new ResponseEntity<>(
                structure.setStatus(status.value())
                        .setMessage(message)
                        .setRootCause(rootCause)
                , status);
    }
}
