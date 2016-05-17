package com.mule.connectors.commons.rest.test.assertion.body;

import com.mule.connectors.commons.rest.test.assertion.ResponseAssertion;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;

/**
 *
 */
public class BodyAssertion extends BaseMatcher<Response> implements ResponseAssertion {
    private Matcher<String> matcher;

    public BodyAssertion(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Object item) {
        return matcher.matches(Response.class.cast(item).readEntity(String.class));
    }

    @Override
    public void describeTo(Description description) {
        matcher.describeTo(description);
    }
}
