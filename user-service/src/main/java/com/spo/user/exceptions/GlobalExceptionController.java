package com.spo.user.exceptions;

import com.spo.user.exceptions.ResourceNotFoundException;
import com.spo.user.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerResourceNotFounException(ResourceNotFoundException resourceNotFoundException) {
        String message = resourceNotFoundException.getMessage();

        ApiResponse response = new ApiResponse().builder()
                .message(message)
                .succes(true)
                .status(HttpStatus.NOT_FOUND)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }
}
