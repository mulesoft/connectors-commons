package com.mule.connectors.commons.rest.test.assertion.body;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hamcrest.core.StringContains;

public class Contains extends BodyAssertion {

    @JsonCreator
    public Contains(@JsonProperty(value = "expected", required = true) String expectedValue) {
        super(new StringContains(expectedValue));
    }
}
