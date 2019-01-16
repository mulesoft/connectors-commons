package com.mulesoft.extensions.request.builder.request;

import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.listener.RequestListener;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.domain.HttpProtocol;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.mule.runtime.http.api.domain.message.BaseHttpMessage;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public abstract class AbstractExecutableRequest<RESULT> extends BaseHttpMessage implements org.mule.runtime.http.api.domain.message.request.HttpRequest {
    private final String path;
    private final Method method;
    private final HttpAuthentication authentication;
    private final MultiMap<String, String> headers;
    private final MultiMap<String, String> queryParams;
    private final HttpEntity entity;
    private final int timeout;
    private final boolean followRedirects;
    private final ResponseHandler<RESULT> responseHandler;
    private final List<RequestListener> requestListeners;

    public AbstractExecutableRequest(String path,
                                     Method method,
                                     HttpAuthentication authentication,
                                     MultiMap<String, String> headers,
                                     MultiMap<String, String> queryParams,
                                     HttpEntity entity,
                                     int timeout,
                                     boolean followRedirects,
                                     List<RequestListener> requestListeners,
                                     ResponseHandler<RESULT> responseHandler) {
        this.path = path;
        this.method = method;
        this.authentication = authentication;
        this.headers = headers;
        this.queryParams = queryParams;
        this.entity = entity;
        this.timeout = timeout;
        this.followRedirects = followRedirects;
        this.requestListeners = requestListeners;
        this.responseHandler = responseHandler;
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

    protected int getTimeout() {
        return timeout;
    }

    protected boolean isFollowRedirects() {
        return followRedirects;
    }

    protected ResponseHandler<RESULT> getResponseHandler() {
        return responseHandler;
    }

    protected HttpAuthentication getAuthentication() {
        return authentication;
    }

    protected List<RequestListener> getRequestListeners() {
        return requestListeners;
    }
}
