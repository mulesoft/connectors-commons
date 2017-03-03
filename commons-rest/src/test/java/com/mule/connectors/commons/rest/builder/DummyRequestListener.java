package com.mule.connectors.commons.rest.builder;

import com.mule.connectors.commons.rest.builder.listener.RequestListener;
import com.mule.connectors.commons.rest.builder.request.Request;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Dummy for {@link RequestListener}.
 */
public class DummyRequestListener implements RequestListener {

    @Override
    public void handle(Request request) {
        assertThat(request, notNullValue());
    }
}
