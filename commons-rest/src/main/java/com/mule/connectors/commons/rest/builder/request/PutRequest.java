package com.mule.connectors.commons.rest.builder.request;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

/**
 * Representation of an HTTP POST Request.
 */
public class PutRequest extends Request {

    @Override
    protected Response doExecute(Invocation.Builder requestBuilder, Object entity, String contentType) {
        return requestBuilder.put(Entity.entity(entity, contentType));
    }
}
