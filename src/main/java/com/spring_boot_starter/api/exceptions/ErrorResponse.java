package com.spring_boot_starter.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String message;
    private String code;

    public ErrorResponse( String message, String code){
        super();
        this.message = message;
        this.code = code;
    }
}
