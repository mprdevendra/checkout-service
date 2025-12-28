package com.demo.exception;

public class PrintPriceServiceException extends RuntimeException{
    public PrintPriceServiceException(String message) {
        super(message);
    }
    public PrintPriceServiceException(String message,Exception exception) {
        super(message,exception);
    }
}
