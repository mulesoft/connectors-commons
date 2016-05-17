package com.mule.connectors.commons.rest.builder.handler;

import javax.ws.rs.core.Response;
import java.lang.reflect.Type;

public interface ResponseHandler<T> {

    T handleResponse(Response response, Type responseType);
}
