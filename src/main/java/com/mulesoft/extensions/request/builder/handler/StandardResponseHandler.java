package com.mulesoft.extensions.request.builder.handler;

import com.mulesoft.extensions.request.builder.exception.RequestFailedException;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class StandardResponseHandler<T> implements ResponseHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultResponseHandler.class);

    @Override
    public T handleResponse(HttpResponse response) {
        Integer statusCode = response.getStatusCode();
        logger.debug("Response Status is {}:{}", statusCode, HttpConstants.HttpStatus.getReasonPhraseForStatusCode(statusCode));
        return handleSuccessfulResponse(Optional.of(response)
                .filter(this::filterSuccess)
                .orElseThrow(() -> handleErrorResponse(response)));
    }

    protected boolean filterSuccess(HttpResponse response) {
        return 200 <= response.getStatusCode() && 300 > response.getStatusCode();
    }

    protected abstract T handleSuccessfulResponse(HttpResponse response);

    protected RequestFailedException handleErrorResponse(HttpResponse response) {
        return new RequestFailedException(response);
    }
}
