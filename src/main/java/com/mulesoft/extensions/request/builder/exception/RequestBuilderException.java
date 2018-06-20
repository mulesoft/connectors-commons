package com.mulesoft.extensions.request.builder.exception;

public class RequestBuilderException extends RuntimeException {

    public RequestBuilderException(String message) {
        super(message);
    }

    public RequestBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
