package com.mule.connectors.commons.rest.builder.listener;

import com.mule.connectors.commons.rest.builder.request.Request;

/**
 * Hook interface that is called before the request is sent to the server and allows the developer to work on it.
 */
public interface RequestListener {

    /**
     * Handles the {@link Request} before it's sent.
     *
     * @param request The request to handle.
     */
    void handle(Request request);
}
