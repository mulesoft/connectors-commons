package com.mule.connectors.commons.rest.test.assertion.header;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.Map;

public class HeaderEquals extends HeaderAssertion {

    private final String expectedKey;
    private final Matcher<Iterable<? super String>> keyMatcher;
    private final Matcher<Iterable<? super String>> valueMatcher;

    @JsonCreator
    public HeaderEquals(@JsonProperty(value = "expectedKey", required = true) String expectedKey, @JsonProperty(value = "expectedValue", required = true) String expectedValue) {
        this.expectedKey = expectedKey;
        this.keyMatcher = CoreMatchers.hasItem(expectedKey);
        this.valueMatcher = CoreMatchers.hasItem(expectedValue);
    }

    @Override
    public boolean matches(Map<String, List<Object>> headers) {
        return keyMatcher.matches(headers.keySet()) && valueMatcher.matches(headers.get(expectedKey));
    }

    @Override
    public void describeTo(Description description) {
        keyMatcher.describeTo(description);
        valueMatcher.describeTo(description);
    }
}
