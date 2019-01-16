package com.mulesoft.extensions.request.builder;

import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.request.AsyncExecutableRequest;
import com.mulesoft.extensions.request.builder.request.Method;

public class AsyncRequestBuilder<RESULT> extends AbstractRequestBuilder<RESULT, AsyncExecutableRequest<RESULT>, AsyncRequestBuilder<RESULT>> {

    public AsyncRequestBuilder(Method method, String path, ResponseHandler<RESULT> responseHandler) {
        super(AsyncExecutableRequest::new, method, path, responseHandler);
    }
}
