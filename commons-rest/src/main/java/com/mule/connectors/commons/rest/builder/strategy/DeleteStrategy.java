package com.mule.connectors.commons.rest.builder.strategy;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

/**
 * {@link RequestMethodStrategy} that used the DELETE method.
 * 
 * @author gaston.ortiz@mulesoft.com
 */
public final class DeleteStrategy implements RequestMethodStrategy {

    @Override
    public Response execute(Builder requestBuilder, Object entity, String contentType) {
        return requestBuilder.delete();
    }

    @Override
    public String toString() {
        return "DELETE";
    }
}
