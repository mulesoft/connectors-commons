package com.mule.connectors.commons.rest.builder.strategy;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

/**
 * Functional interface containing the method that does the request to the endpoint.
 * 
 * @author gaston.ortiz@mulesoft.com
 */
public interface RequestMethodStrategy {

    Response execute(Builder requestBuilder, Object entity, String contentType);
}
