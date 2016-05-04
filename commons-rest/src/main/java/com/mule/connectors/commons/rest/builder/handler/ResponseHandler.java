package com.mule.connectors.commons.rest.builder.handler;

import javax.ws.rs.core.Response;

public interface ResponseHandler<T> {

    T handleResponse(Response response, Class<T> responseType);
}
