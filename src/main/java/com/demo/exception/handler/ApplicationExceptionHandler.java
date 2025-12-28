package com.demo.exception.handler;

import com.demo.exception.ApplicationException;
import com.demo.exception.response.CustomErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException exception) {
        Map<String, String> exceptionMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                exceptionMap.put(error.getField(), error.getDefaultMessage())
        );
        CustomErrorResponse error = buildErrorResponse(exception.getStatusCode().value(), exceptionMap.toString());
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException exception) {
        CustomErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationLayerException(ApplicationException exception) {
        CustomErrorResponse errorResponse = buildErrorResponse(exception.getErrorCode(), exception.getMessage());
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception) {
        CustomErrorResponse errorResponse = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private CustomErrorResponse buildErrorResponse(int statusCode, String exceptionMessage){
        return CustomErrorResponse.builder()
                .status(statusCode)
                .errorMessage(exceptionMessage)
                .build();
    }
}
