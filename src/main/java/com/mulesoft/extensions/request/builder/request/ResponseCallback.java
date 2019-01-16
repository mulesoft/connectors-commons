package com.mulesoft.extensions.request.builder.request;

public interface ResponseCallback<RESULT> {
    void onSuccess(RESULT result);

    void onFailure(Throwable cause);
}
