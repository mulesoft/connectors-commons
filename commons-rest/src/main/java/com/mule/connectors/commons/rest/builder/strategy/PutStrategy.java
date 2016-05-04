package com.mule.connectors.commons.rest.builder.strategy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

/**
 * {@link RequestMethodStrategy} that used the PUT method.
 * 
 * @author gaston.ortiz@mulesoft.com
 */
public final class PutStrategy implements RequestMethodStrategy {

    @Override
    public Response execute(Builder requestBuilder, Object entity, String contentType) {
        return requestBuilder.put(Entity.entity(entity, contentType));
    }

    @Override
    public String toString() {
        return "PUT";
    }
}
