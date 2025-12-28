package com.demo.exception;

public class DTOMapperServiceException extends RuntimeException {
    public DTOMapperServiceException(String message) {
        super(message);
    }
    public DTOMapperServiceException(String message,Exception exception) {
        super(message,exception);
    }
}
