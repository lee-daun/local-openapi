package com.local.openapi.core.exception;

public class ServiceException extends RuntimeException{
    private Integer statusCode;
    public ServiceException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
