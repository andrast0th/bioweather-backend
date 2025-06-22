package com.example.bioweatherbackend.api;

import com.example.bioweatherbackend.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handle(Exception ex){

        var statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (ex instanceof NoHandlerFoundException) {
            statusCode = HttpStatus.NOT_FOUND.value();
        } else if (ex instanceof IllegalArgumentException) {
            statusCode = HttpStatus.BAD_REQUEST.value();
        } else if (ex instanceof IllegalStateException) {
            statusCode = HttpStatus.CONFLICT.value();
        }

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), statusCode);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(statusCode));
    }
}