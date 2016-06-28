package com.mule.connectors.commons.rest.test.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponseAssertion;
import com.mule.connectors.commons.rest.test.assertion.status.Status;
import com.mule.connectors.commons.rest.test.provider.request.ParameterizedRequest;
import com.mule.connectors.commons.rest.test.provider.request.ResponseTarget;
import org.hamcrest.Matcher;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Class representing a test case for Rest Testing Commons API.
 */
public class TestCase {

    private static final Status STATUS_MATCHER = new Status("2xx");
    private final ParameterizedRequest request;
    private final List<RequestAndResponseAssertion> assertions;
    private final List<ParameterizedRequest> setupAbstractRequestList;
    private final List<ParameterizedRequest> tearDownRequestList;

    @JsonCreator
    public TestCase(@JsonProperty(value = "before", required = true) List<ParameterizedRequest> setupAbstractRequestList,
            @JsonProperty(value = "request", required = true) ParameterizedRequest request,
            @JsonProperty(value = "after", required = true) List<ParameterizedRequest> tearDownRequestList,
            @JsonProperty(value = "assertions", required = true) List<RequestAndResponseAssertion> assertions) {
        this.setupAbstractRequestList = Optional.fromNullable(setupAbstractRequestList).or(Collections.<ParameterizedRequest>emptyList());
        this.request = request;
        this.tearDownRequestList = Optional.fromNullable(tearDownRequestList).or(Collections.<ParameterizedRequest>emptyList());
        this.assertions = Optional.fromNullable(assertions).or(Collections.<RequestAndResponseAssertion>emptyList());
    }

    public void execute(Client client) {
        Map<String, String> placeholderStore = new HashMap<>();
        for (ParameterizedRequest request : setupAbstractRequestList) {
            placeholderStore = execute(request, placeholderStore, client, STATUS_MATCHER);
        }
        List<Matcher<? super RequestAndResponse>> matchers = new ArrayList<>();
        matchers.addAll(this.assertions);
        placeholderStore = execute(request, placeholderStore, client, allOf(matchers));
        for (ParameterizedRequest request : tearDownRequestList) {
            placeholderStore = execute(request, placeholderStore, client, STATUS_MATCHER);
        }
    }

    private Map<String, String> execute(ParameterizedRequest request, Map<String, String> placeholderStore, Client client, Matcher<? super RequestAndResponse> matcher) {
        request.addPlaceHolders(placeholderStore);
        Response response = request.execute(client);
        assertThat(new RequestAndResponse(request, response), matcher);
        Map<String, String> result = new HashMap<>();
        String parsedResponse = response.readEntity(String.class);
        for (ResponseTarget responseTarget : request.getResponseTargets()) {
            result.put(responseTarget.getKey(), responseTarget.match(parsedResponse));
        }
        return result;
    }
}
