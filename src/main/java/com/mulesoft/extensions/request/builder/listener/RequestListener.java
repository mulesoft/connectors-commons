package com.mulesoft.extensions.request.builder.listener;

import org.mule.runtime.http.api.domain.message.request.HttpRequest;

/**
 * Hook interface that is called before the request is sent to the server and allows the developer to work on it.
 */
public interface RequestListener {

    /**
     * Handles the {@link HttpRequest} before it's sent.
     *
     * @param request The request to handle.
     */
    void handle(HttpRequest request);
}
