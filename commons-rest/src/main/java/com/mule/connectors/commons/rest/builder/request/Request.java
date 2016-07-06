package com.mule.connectors.commons.rest.builder.request;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Representation of an HTTP request to be sent.
 */
public interface Request {
    Response execute(Client client);
    String getPath();
    Map<String, String> getHeaders();
    Map<String, String> getQueryParams();
    Map<String, String> getPathParams();
    Object getEntity();
    String getContentType();
    String getAccept();
    Method getMethod();
}
