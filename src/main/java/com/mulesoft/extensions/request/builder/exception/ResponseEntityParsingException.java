package com.mulesoft.extensions.request.builder.exception;

public class ResponseEntityParsingException extends RequestBuilderException {
    public ResponseEntityParsingException(Throwable cause) {
        super("An error occurred while parsing the response's entity.", cause);
    }
}
