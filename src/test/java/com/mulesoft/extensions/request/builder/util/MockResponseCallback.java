package com.mulesoft.extensions.request.builder.util;

import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.request.ResponseCallback;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MockResponseCallback<T> implements ResponseCallback<T> {

    private final Class<? extends Throwable> expectedException;
    private final HttpResponse expectedResponse;
    private final ResponseHandler<T> responseHandler;

    public MockResponseCallback(ResponseHandler<T> responseHandler, HttpResponse expectedResponse, Class<? extends Throwable> expectedException) {
        this.expectedResponse = expectedResponse;
        this.responseHandler = responseHandler;
        this.expectedException = expectedException;
    }

    @Override
    public void onSuccess(T result) {
        assertThat(expectedResponse, notNullValue());
        assertThat(result, equalTo(responseHandler.handleResponse(expectedResponse)));
    }

    @Override
    public void onFailure(Throwable cause) {
        assertThat(expectedException, notNullValue());
        assertThat(cause, instanceOf(expectedException));
    }
}
