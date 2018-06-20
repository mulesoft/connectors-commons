package com.mulesoft.extensions.request.builder.exception;

import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import static java.lang.String.format;

public class RequestFailedException extends RequestBuilderException {

    private final int statusCode;
    private final transient HttpResponse response;

    public RequestFailedException(HttpResponse response) {
        this(response, format("[%s] - %s", response.getStatusCode(), HttpConstants.HttpStatus.getReasonPhraseForStatusCode(response.getStatusCode())));
    }

    public RequestFailedException(HttpResponse response, String message) {
        super(message);
        this.statusCode = response.getStatusCode();
        this.response = response;
    }


    public RequestFailedException(HttpResponse response, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = response.getStatusCode();
        this.response = response;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public HttpResponse getResponse() {
        return response;
    }
}
