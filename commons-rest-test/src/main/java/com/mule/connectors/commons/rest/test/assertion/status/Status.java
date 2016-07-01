package com.mule.connectors.commons.rest.test.assertion.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponseAssertion;
import com.mule.connectors.commons.rest.test.exception.StatusCodeDefinitionException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;
import java.util.regex.Pattern;

/**
 * {@link Matcher} that validates that the status code of a {@link Response} is the expected value, which can either be a status code (three digit integer) or a family (2xx or 4xx
 * for example).
 */
public class Status extends BaseMatcher<RequestAndResponse> implements RequestAndResponseAssertion {

    private static final Pattern PATTERN = Pattern.compile("\\d[\\dxX]{2}");
    private final String expectedValue;
    private final Matcher<String> matcher;

    @JsonCreator
    public Status(@JsonProperty(value = "expectedValue", required = true) String expectedValue) {
        if (!PATTERN.matcher(expectedValue).matches()) {
            throw new StatusCodeDefinitionException(expectedValue);
        }
        this.expectedValue = expectedValue;
        matcher = CoreMatchers.startsWith(expectedValue.toLowerCase().replace("x", ""));
    }

    @Override
    public boolean matches(Object item) {
        return matcher.matches(Integer.toString(getResponse(item).getStatus()));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Response status matching ").appendValue(expectedValue);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        super.describeMismatch(getResponse(item).getStatus(), description);
    }

    private Response getResponse(Object item) {
        return RequestAndResponse.class.cast(item).getResponse();
    }
}
