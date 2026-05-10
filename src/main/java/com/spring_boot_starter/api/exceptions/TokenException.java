package com.spring_boot_starter.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenException extends RuntimeException {
    private String message;

    public TokenException() {}

    public TokenException(String message) {
        super(message);
        this.message = message;
    }
}
