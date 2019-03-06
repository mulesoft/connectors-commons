package com.mulesoft.extensions.request.builder;

import com.mulesoft.extensions.request.builder.handler.DefaultResponseHandler;
import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.request.Method;

import static com.mulesoft.extensions.request.builder.request.Method.*;

public interface RequestBuilder {

    static <T> SyncRequestBuilder<T> get(String path, ResponseHandler<T> responseHandler) {
        return request(GET, path, responseHandler);
    }

    static SyncRequestBuilder<String> get(String path) {
        return request(GET, path);
    }

    static <T> SyncRequestBuilder<T> post(String path, ResponseHandler<T> responseHandler) {
        return request(POST, path, responseHandler);
    }

    static SyncRequestBuilder<String> post(String path) {
        return request(POST, path);
    }

    static <T> SyncRequestBuilder<T> put(String path, ResponseHandler<T> responseHandler) {
        return request(PUT, path, responseHandler);
    }

    static SyncRequestBuilder<String> put(String path) {
        return request(PUT, path);
    }

    static <T> SyncRequestBuilder<T> delete(String path, ResponseHandler<T> responseHandler) {
        return request(DELETE, path, responseHandler);
    }

    static SyncRequestBuilder<String> delete(String path) {
        return request(DELETE, path);
    }

    static <T> SyncRequestBuilder<T> patch(String path, ResponseHandler<T> responseHandler) {
        return request(PATCH, path, responseHandler);
    }

    static SyncRequestBuilder<String> patch(String path) {
        return request(PATCH, path);
    }

    static <T> SyncRequestBuilder<T> head(String path, ResponseHandler<T> responseHandler) {
        return request(HEAD, path, responseHandler);
    }

    static SyncRequestBuilder<String> head(String path) {
        return request(HEAD, path);
    }

    static SyncRequestBuilder<String> options(String path) {
        return request(OPTIONS, path);
    }

    static <T> SyncRequestBuilder<T> options(String path, ResponseHandler<T> responseHandler) {
        return request(OPTIONS, path, responseHandler);
    }

    static SyncRequestBuilder<String> request(Method method, String path) {
        return request(method, path, new DefaultResponseHandler());
    }

    static <T> SyncRequestBuilder<T> request(Method method, String path, ResponseHandler<T> responseHandler) {
        return new SyncRequestBuilder<>(method, path, responseHandler);
    }

    static <T> AsyncRequestBuilder<T> asyncGet(String path, ResponseHandler<T> responseHandler) {
        return asyncRequest(GET, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncGet(String path) {
        return asyncRequest(GET, path);
    }

    static <T> AsyncRequestBuilder<T> asyncPost(String path, ResponseHandler<T> responseHandler) {
        return asyncRequest(POST, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncPost(String path) {
        return asyncRequest(POST, path);
    }

    static <T> AsyncRequestBuilder<T> asyncPut(String path, ResponseHandler<T> responseHandler) {
        return asyncRequest(PUT, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncPut(String path) {
        return asyncRequest(PUT, path);
    }

    static <T> AsyncRequestBuilder<T> asyncDelete(String path, ResponseHandler<T> responseHandler) {
        return asyncRequest(DELETE, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncDelete(String path) {
        return asyncRequest(DELETE, path);
    }

    static <T> AsyncRequestBuilder<T> asyncPatch(String path, ResponseHandler<T> responseHandler) {
        return asyncRequest(PATCH, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncPatch(String path) {
        return asyncRequest(PATCH, path);
    }

    static <T> AsyncRequestBuilder<T> asyncHead(String path, ResponseHandler<T> responseHandler) {
        return asyncRequest(HEAD, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncHead(String path) {
        return asyncRequest(HEAD, path);
    }

    static <T> AsyncRequestBuilder<T> asyncOptions(String path, ResponseHandler<T> responseHandler) {
        return asyncRequest(OPTIONS, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncOptions(String path) {
        return asyncRequest(OPTIONS, path);
    }

    static AsyncRequestBuilder<String> asyncRequest(Method method, String path) {
        return asyncRequest(method, path, new DefaultResponseHandler());
    }

    static <T> AsyncRequestBuilder<T> asyncRequest(Method method, String path, ResponseHandler<T> responseHandler) {
        return new AsyncRequestBuilder<>(method, path, responseHandler);
    }
}
