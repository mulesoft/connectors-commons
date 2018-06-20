package com.mulesoft.extensions.request.builder.request;

import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.domain.HttpProtocol;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.mule.runtime.http.api.domain.message.BaseHttpMessage;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collection;

public class SimpleRequest extends BaseHttpMessage implements HttpRequest {
    private final String path;
    private final Method method;
    private final MultiMap<String, String> headers;
    private final MultiMap<String, String> queryParams;
    private final HttpEntity entity;

    public SimpleRequest(String path,
                         Method method,
                         MultiMap<String, String> headers,
                         MultiMap<String, String> queryParams,
                         HttpEntity entity) {
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.queryParams = queryParams;
        this.entity = entity;
    }

    @Override
    public HttpProtocol getProtocol() {
        return null;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public MultiMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public MultiMap<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public HttpEntity getEntity() {
        return entity;
    }

    @Override
    public String getMethod() {
        return method.name();
    }

    @Override
    public URI getUri() {
        return URI.create(getPath());
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public String getHeaderValue(String headerName) {
        return headers.get(headerName);
    }

    @Override
    public Collection<String> getHeaderValues(String headerName) {
        return headers.getAll(headerName);
    }
}
