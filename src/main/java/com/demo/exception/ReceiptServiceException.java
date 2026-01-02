package com.demo.exception;

public class ReceiptServiceException extends RuntimeException {
    public ReceiptServiceException(String message) {
        super(message);
    }
    public ReceiptServiceException(String message, Exception exception) {
        super(message,exception);
    }
}
