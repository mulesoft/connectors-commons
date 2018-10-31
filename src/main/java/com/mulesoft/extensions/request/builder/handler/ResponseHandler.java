package com.mulesoft.extensions.request.builder.handler;

import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.lang.reflect.Type;

@FunctionalInterface
public interface ResponseHandler<T> {

    T handleResponse(HttpResponse response);
}
