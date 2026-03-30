package com.coop.api.exceptions;

public class OtpException extends RuntimeException {
    private String message;

    public OtpException() {}

    public OtpException(String message) {
        super(message);
        this.message = message;
    }
}
