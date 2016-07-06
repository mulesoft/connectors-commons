package com.mule.connectors.commons.rest.test.provider;

import com.mule.connectors.commons.rest.builder.request.Request;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponseAssertion;
import com.mule.connectors.commons.rest.test.provider.request.ParameterizedRequest;
import com.mule.connectors.commons.rest.test.provider.request.ResponseTarget;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;
import static java.math.BigInteger.TEN;
import static org.easymock.EasyMock.*;

public class TestCaseTest {

    private List<ParameterizedRequest> setupRequests;
    private List<ParameterizedRequest> tearDownRequests;
    private ParameterizedRequest parameterizedRequest;
    private TestCase testCase;
    private List<RequestAndResponseAssertion> assertions;
    private Client client;
    private Response response;
    private RequestAndResponseAssertion matcher;

    @Before
    public void setup() {
        setupRequests = new ArrayList<>();
        tearDownRequests = new ArrayList<>();
        parameterizedRequest = mock(ParameterizedRequest.class);
        assertions = new ArrayList<>();
        testCase = new TestCase(setupRequests, parameterizedRequest, tearDownRequests, assertions);
        client = mock(Client.class);
        response = mock(Response.class);
        matcher = mock(RequestAndResponseAssertion.class);
    }

    @Test
    public void executeTest() {
        execute();
    }

    @Test
    public void executeWithSetupTest() {
        for (int i = 0; i < TEN.intValue(); i++) {
            setupRequests.add(mock(ParameterizedRequest.class));
        }
        execute();
    }

    @Test
    public void executeWithTearDownTest() {
        for (int i = 0; i < TEN.intValue(); i++) {
            tearDownRequests.add(mock(ParameterizedRequest.class));
        }
        execute();
    }

    private void execute() {
        parameterizedRequest.addPlaceHolders((Map<String, String>) anyObject(Map.class));
        expectLastCall();
        expect(parameterizedRequest.getResponseTargets()).andReturn(new ArrayList<ResponseTarget>());
        expect(parameterizedRequest.execute(same(client))).andReturn(response);
        expect(response.readEntity(eq(String.class))).andReturn("TEST");
        expect(response.getStatus()).andReturn(Response.Status.OK.getStatusCode()).anyTimes();
        stubPerifericRequests(setupRequests);
        stubPerifericRequests(tearDownRequests);
        expect(matcher.matches(anyObject(RequestAndResponse.class))).andReturn(TRUE);
        assertions.add(matcher);
        replay(parameterizedRequest, response, client, matcher);
        testCase.execute(client);
        verify(parameterizedRequest, response, client, matcher);
        for (Request setupRequest : setupRequests) {
            verify(setupRequest);
        }
    }

    private void stubPerifericRequests(List<ParameterizedRequest> requests) {
        for (ParameterizedRequest request : requests) {
            expect(request.getResponseTargets()).andReturn(new ArrayList<ResponseTarget>());
            request.addPlaceHolders((Map<String, String>) anyObject(Map.class));
            expect(response.readEntity(eq(String.class))).andReturn("TEST");
            expectLastCall();
            expect(request.execute(same(client))).andReturn(response);
            replay(request);
        }
    }
}
