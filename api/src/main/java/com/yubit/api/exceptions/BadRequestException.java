package com.yubit.api.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {}
    public BadRequestException(String message) {}
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
