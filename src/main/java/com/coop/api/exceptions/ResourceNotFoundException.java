package com.coop.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private  String message;

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
