package com.demo.exception;

public class CheckoutServiceException extends ApplicationException {
    public CheckoutServiceException(String message) {
        super(message);
    }
    public CheckoutServiceException(int errorCode, String message) {
        super(errorCode,message);
    }
    public CheckoutServiceException(int errorCode, String message, Exception exception) {
        super(errorCode,message,exception);
    }
}
