package com.mulesoft.extensions.request.builder.exception;

import org.junit.Test;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestFailedExceptionTest {
    private static final Integer STATUS_CODE = 400;
    private static final String MESSAGE = "Message.";
    private static final Throwable CAUSE = new RuntimeException("Cause");


    @Test
    public void testExposedConstructor() {
        HttpResponse response = mock(HttpResponse.class);
        expect(response.getStatusCode()).andReturn(STATUS_CODE);
        replay(response);
        RequestFailedException exception = new RequestFailedException(response, MESSAGE, CAUSE);
        assertThat(exception.getStatusCode(), equalTo(STATUS_CODE));
        assertThat(exception.getResponse(), equalTo(response));
        assertThat(exception.getMessage(), equalTo(MESSAGE));
        assertThat(exception.getCause(), equalTo(CAUSE));
    }
}
