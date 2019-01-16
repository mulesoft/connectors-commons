package com.mulesoft.extensions.request.builder.exception;

import java.util.concurrent.TimeoutException;

import static java.lang.String.format;

public class RequestTimeoutException extends RequestBuilderException {
    public RequestTimeoutException(int timeout, TimeoutException cause) {
        super(format("Operation timed out after %s milliseconds.", timeout), cause);
    }
}
