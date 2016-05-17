package com.mule.connectors.commons.rest.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mule.connectors.commons.rest.builder.request.Request;
import com.mule.connectors.commons.rest.test.assertion.ResponseAssertion;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Class representing a test case for Rest Testing Commons API.
 */
public class TestCase {

    private Request request;
    private List<ResponseAssertion> responseAssertions;

    @JsonCreator
    public TestCase(@JsonProperty(value = "request", required = true) Request request,
            @JsonProperty(value = "assertions", required = true) List<ResponseAssertion> responseAssertions) {
        this.request = request;
        this.responseAssertions = responseAssertions;
    }

    public TestCaseResult execute(Client client) {
        TestCaseResult result = new TestCaseResult();
        Response response = request.execute(client);
        for (ResponseAssertion responseAssertion : responseAssertions) {
            try {
                assertThat(response, responseAssertion);
            } catch (AssertionError e) {
                result.addError(e.getLocalizedMessage());
            }
        }
        return result;
    }
}
