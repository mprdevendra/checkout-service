package com.demo.exception;

public class PriceCalculationException extends RuntimeException{

    public PriceCalculationException(String message) {
        super(message);
    }
    public PriceCalculationException(String message,Exception exception) {
        super(message,exception);
    }
}
