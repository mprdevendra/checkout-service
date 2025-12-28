package com.demo.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(String message,Exception exception) {
        super(message,exception);
    }
}
