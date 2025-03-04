package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handle(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}