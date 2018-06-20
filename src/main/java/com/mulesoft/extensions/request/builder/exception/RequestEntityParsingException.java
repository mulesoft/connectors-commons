package com.mulesoft.extensions.request.builder.exception;

public class RequestEntityParsingException extends RequestBuilderException {
    public RequestEntityParsingException(Throwable cause) {
        super("An error occurred while parsing the request's entity.", cause);
    }
}
