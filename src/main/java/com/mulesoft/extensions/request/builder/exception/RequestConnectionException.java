package com.mulesoft.extensions.request.builder.exception;

import java.io.IOException;

public class RequestConnectionException extends RequestBuilderException {
    public RequestConnectionException(IOException cause) {
        super("An error occurred while connecting.", cause);
    }
}
