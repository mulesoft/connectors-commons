package com.mule.connectors.commons.rest.test.assertion.header;

import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponseAssertion;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * {@link Matcher} that applies a determined String Matcher to the headers of a {@link Response}.
 */
public abstract class ResponseHeaderAssertion extends BaseMatcher<RequestAndResponse> implements RequestAndResponseAssertion {

    @Override
    public boolean matches(Object item) {
        return matches(getResponse(item).getHeaders());
    }

    public abstract boolean matches(Map<String, List<Object>> headers);

    private Response getResponse(Object item) {
        return RequestAndResponse.class.cast(item).getResponse();
    }
}
