package com.demo.exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException(String message) {
        super(message);
    }
    public InvalidInputException(String message,Exception exception) {
        super(message,exception);
    }
}
