package com.mulesoft.extensions.request.builder.request;

import com.mulesoft.extensions.request.builder.exception.RequestConnectionException;
import com.mulesoft.extensions.request.builder.exception.RequestTimeoutException;
import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.listener.RequestListener;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public class AsyncExecutableRequest<RESULT> extends AbstractExecutableRequest<RESULT> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractExecutableRequest.class);

    public AsyncExecutableRequest(String path,
                                  Method method,
                                  HttpAuthentication authentication,
                                  MultiMap<String, String> headers,
                                  MultiMap<String, String> queryParams,
                                  HttpEntity entity,
                                  int timeout,
                                  boolean followRedirects,
                                  List<RequestListener> requestListeners,
                                  ResponseHandler<RESULT> responseHandler) {
        super(path, method, authentication, headers, queryParams, entity, timeout, followRedirects, requestListeners, responseHandler);
    }

    public void execute(HttpClient httpClient, ResponseCallback<RESULT> onComplete) {
        getRequestListeners().stream()
                .peek(listener -> logger.debug("Request Listener {} found. Providing request.", listener.getClass()))
                .forEach(listener -> listener.handle(this));
        httpClient.sendAsync(this, getTimeout(), isFollowRedirects(), getAuthentication()).whenCompleteAsync(((httpResponse, throwable) -> {
            Optional.ofNullable(httpResponse).ifPresent(response -> {
                try {
                    onComplete.onSuccess(getResponseHandler().handleResponse(httpResponse));
                } catch (RuntimeException e) {
                    onComplete.onFailure(e);
                }
            });
            Optional.ofNullable(Optional.ofNullable(throwable)
                    .filter(TimeoutException.class::isInstance)
                    .map(TimeoutException.class::cast)
                    .<Throwable>map(exception -> new RequestTimeoutException(getTimeout(), exception))
                    .orElse(Optional.ofNullable(throwable)
                            .filter(IOException.class::isInstance)
                            .map(IOException.class::cast)
                            .<Throwable>map(RequestConnectionException::new)
                            .orElse(throwable)))
                    .ifPresent(onComplete::onFailure);
        }));

    }
}
