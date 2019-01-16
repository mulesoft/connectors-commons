package com.mulesoft.extensions.request.builder;

import com.mulesoft.extensions.request.builder.handler.DefaultResponseHandler;
import com.mulesoft.extensions.request.builder.handler.ResponseHandler;

import static com.mulesoft.extensions.request.builder.request.Method.*;

public interface RequestBuilder {

    static <T> SyncRequestBuilder<T> get(String path, ResponseHandler<T> responseHandler) {
        return new SyncRequestBuilder<>(GET, path, responseHandler);
    }

    static SyncRequestBuilder<String> get(String path) {
        return get(path, new DefaultResponseHandler());
    }

    static <T> SyncRequestBuilder<T> post(String path, ResponseHandler<T> responseHandler) {
        return new SyncRequestBuilder<>(POST, path, responseHandler);
    }

    static SyncRequestBuilder<String> post(String path) {
        return post(path, new DefaultResponseHandler());
    }

    static <T> SyncRequestBuilder<T> put(String path, ResponseHandler<T> responseHandler) {
        return new SyncRequestBuilder<>(PUT, path, responseHandler);
    }

    static SyncRequestBuilder<String> put(String path) {
        return put(path, new DefaultResponseHandler());
    }

    static <T> SyncRequestBuilder<T> delete(String path, ResponseHandler<T> responseHandler) {
        return new SyncRequestBuilder<>(DELETE, path, responseHandler);
    }

    static SyncRequestBuilder<String> delete(String path) {
        return delete(path, new DefaultResponseHandler());
    }

    static <T> SyncRequestBuilder<T> patch(String path, ResponseHandler<T> responseHandler) {
        return new SyncRequestBuilder<>(PATCH, path, responseHandler);
    }

    static SyncRequestBuilder<String> patch(String path) {
        return patch(path, new DefaultResponseHandler());
    }

    static <T> SyncRequestBuilder<T> head(String path, ResponseHandler<T> responseHandler) {
        return new SyncRequestBuilder<>(HEAD, path, responseHandler);
    }

    static SyncRequestBuilder<String> head(String path) {
        return head(path, new DefaultResponseHandler());
    }

    static <T> SyncRequestBuilder<T> options(String path, ResponseHandler<T> responseHandler) {
        return new SyncRequestBuilder<>(OPTIONS, path, responseHandler);
    }

    static SyncRequestBuilder<String> options(String path) {
        return options(path, new DefaultResponseHandler());
    }

    static <T> AsyncRequestBuilder<T> asyncGet(String path, ResponseHandler<T> responseHandler) {
        return new AsyncRequestBuilder<>(GET, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncGet(String path) {
        return asyncGet(path, new DefaultResponseHandler());
    }

    static <T> AsyncRequestBuilder<T> asyncPost(String path, ResponseHandler<T> responseHandler) {
        return new AsyncRequestBuilder<>(POST, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncPost(String path) {
        return asyncPost(path, new DefaultResponseHandler());
    }

    static <T> AsyncRequestBuilder<T> asyncPut(String path, ResponseHandler<T> responseHandler) {
        return new AsyncRequestBuilder<>(PUT, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncPut(String path) {
        return asyncPut(path, new DefaultResponseHandler());
    }

    static <T> AsyncRequestBuilder<T> asyncDelete(String path, ResponseHandler<T> responseHandler) {
        return new AsyncRequestBuilder<>(DELETE, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncDelete(String path) {
        return asyncDelete(path, new DefaultResponseHandler());
    }

    static <T> AsyncRequestBuilder<T> asyncPatch(String path, ResponseHandler<T> responseHandler) {
        return new AsyncRequestBuilder<>(PATCH, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncPatch(String path) {
        return asyncPatch(path, new DefaultResponseHandler());
    }

    static <T> AsyncRequestBuilder<T> asyncHead(String path, ResponseHandler<T> responseHandler) {
        return new AsyncRequestBuilder<>(HEAD, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncHead(String path) {
        return asyncHead(path, new DefaultResponseHandler());
    }

    static <T> AsyncRequestBuilder<T> asyncOptions(String path, ResponseHandler<T> responseHandler) {
        return new AsyncRequestBuilder<>(OPTIONS, path, responseHandler);
    }

    static AsyncRequestBuilder<String> asyncOptions(String path) {
        return asyncOptions(path, new DefaultResponseHandler());
    }
}
