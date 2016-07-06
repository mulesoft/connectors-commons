package com.mule.connectors.commons.rest.test.assertion.body;

import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponseAssertion;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;

/**
 * {@link Matcher} that applies a determined String Matcher to the body of a {@link Response}.
 */
public abstract class ResponseBodyAssertion extends BaseMatcher<RequestAndResponse> implements RequestAndResponseAssertion {
    private Matcher<String> matcher;

    public ResponseBodyAssertion(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Object item) {
        return matcher.matches(getResponse(item).readEntity(String.class));
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        super.describeMismatch(getResponse(item).readEntity(String.class), description);
    }

    private Response getResponse(Object item) {
        return RequestAndResponse.class.cast(item).getResponse();
    }
}
