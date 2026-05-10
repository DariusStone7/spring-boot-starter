package com.spring_boot_starter.api.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{

    private String message;

    public ResourceAlreadyExistsException(){}

    public ResourceAlreadyExistsException(String message){
        super(message);
        this.message = message;
    }
}
