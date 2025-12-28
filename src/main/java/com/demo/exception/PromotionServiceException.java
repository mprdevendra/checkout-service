package com.demo.exception;

public class PromotionServiceException extends RuntimeException {
    public PromotionServiceException(String message) {
        super(message);
    }
    public PromotionServiceException(String message,Exception exception) {
        super(message,exception);
    }
}
