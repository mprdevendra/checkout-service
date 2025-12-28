package com.demo.exception;

public abstract class ApplicationException extends RuntimeException {

    private int errorCode;

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(String message, Exception exception) {
        super(message, exception);
    }

    public ApplicationException(int errorCode, String message, Exception exception) {
        super(message, exception);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
