package com.mulesoft.extensions.request.builder.request;

import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.listener.RequestListener;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;

import java.util.List;

@FunctionalInterface
public interface ExecutableRequestConstructor<RESULT, TYPE extends HttpRequest> {
    TYPE create(String path,
                Method method,
                HttpAuthentication authentication,
                MultiMap<String, String> headers,
                MultiMap<String, String> queryParams,
                HttpEntity entity,
                int timeout,
                boolean followRedirects,
                List<RequestListener> requestListeners,
                ResponseHandler<RESULT> responseHandler);
}
