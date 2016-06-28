package com.mule.connectors.commons.rest.test.assertion.body;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hamcrest.Description;
import org.hamcrest.core.StringContains;

/**
 * {@link ResponseBodyAssertion} that verifies that the body body the expected value.
 */
public class Contains extends ResponseBodyAssertion {

    private final String expectedValue;

    @JsonCreator
    public Contains(@JsonProperty(value = "expected", required = true) String expectedValue) {
        super(new StringContains(expectedValue));
        this.expectedValue = expectedValue;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("SimpleRequest body containing ").appendValue(expectedValue);
    }
}
