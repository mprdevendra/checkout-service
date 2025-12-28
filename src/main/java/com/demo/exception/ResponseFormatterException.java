package com.demo.exception;

public class ResponseFormatterException extends RuntimeException {
    public ResponseFormatterException(String message) {
        super(message);
    }
    public ResponseFormatterException(String message,Exception exception) {
        super(message,exception);
    }
}
