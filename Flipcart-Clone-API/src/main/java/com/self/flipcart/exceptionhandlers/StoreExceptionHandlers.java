package com.self.flipcart.exceptionhandlers;

import com.self.flipcart.exceptions.InvalidDisplayTypeException;
import com.self.flipcart.exceptions.InvalidPrimeCategoryException;
import com.self.flipcart.exceptions.StoreNotFoundByIdException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@AllArgsConstructor
public class StoreExceptionHandlers {

    private ErrorResponse errorResponse;

    @ExceptionHandler(InvalidDisplayTypeException.class)
    public ResponseEntity<Object> handleInvalidDisplayType(InvalidDisplayTypeException ex){
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid display type specified, should be any of basic, card or complete");
    }

    @ExceptionHandler(StoreNotFoundByIdException.class)
    public ResponseEntity<Object> handleStoreNotFoundById(StoreNotFoundByIdException ex){
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "No store present with the given ID");
    }

    @ExceptionHandler(InvalidPrimeCategoryException.class)
    public ResponseEntity<Object> handleInvalidPrimeCategory(InvalidPrimeCategoryException ex){
        return errorResponse.structure(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid prime category specified");
    }
}
