package com.mulesoft.extensions.request.builder;

import com.mulesoft.extensions.request.builder.listener.RequestListener;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Dummy for {@link RequestListener}.
 */
public class DummyRequestListener implements RequestListener {

    @Override
    public void handle(HttpRequest request) {
        assertThat(request, notNullValue());
    }
}
