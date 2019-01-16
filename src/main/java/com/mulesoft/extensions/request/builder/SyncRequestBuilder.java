package com.mulesoft.extensions.request.builder;

import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.request.Method;
import com.mulesoft.extensions.request.builder.request.SyncExecutableRequest;

public class SyncRequestBuilder<RESULT> extends AbstractRequestBuilder<RESULT, SyncExecutableRequest<RESULT>, SyncRequestBuilder<RESULT>> {

    public SyncRequestBuilder(Method method, String path, ResponseHandler<RESULT> responseHandler) {
        super(SyncExecutableRequest::new, method, path, responseHandler);
    }
}
