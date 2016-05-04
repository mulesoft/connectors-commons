package com.mule.connectors.commons.rest.builder.strategy;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

/**
 * {@link RequestMethodStrategy} that used the GET method.
 * 
 * @author gaston.ortiz@mulesoft.com
 */
public final class GetStrategy implements RequestMethodStrategy {

    @Override
    public Response execute(Builder requestBuilder, Object entity, String contentType) {
        return requestBuilder.get();
    }

    @Override
    public String toString() {
        return "GET";
    }
}
