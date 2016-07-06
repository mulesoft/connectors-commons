package com.mule.connectors.commons.rest.test.assertion.status;

import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import com.mule.connectors.commons.rest.test.exception.StatusCodeDefinitionException;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StatusTest {
    private static final String DEFAULT_EXPECTED_VALUE = "2xx";
    private Response response;
    private RequestAndResponse requestAndResponse;

    @Before
    public void setup () {
        response = mock(Response.class);
        requestAndResponse = mock(RequestAndResponse.class);
        expect(requestAndResponse.getResponse()).andReturn(response);
    }

    @Test
    public void validate200WithFamily() {
        assertThat(match(OK.getStatusCode(), DEFAULT_EXPECTED_VALUE), is(true));
    }

    @Test
    public void validate404WithEquals() {
        assertThat(match(NOT_FOUND.getStatusCode(), "404"), is(true));
    }

    @Test
    public void validate200WithIncorrectFamily() {
        assertThat(match(200, "4xx"), is(false));
    }

    @Test
    public void validate404WithIncorrectEquals() {
        assertThat(match(NOT_FOUND.getStatusCode(), "400"), is(false));
    }

    @Test(expected = StatusCodeDefinitionException.class)
    public void invalidFamily() {
        match(OK.getStatusCode(), "2xxx");
    }

    private boolean match(int responseStatus, String expectedStatus) {
        expect(response.getStatus()).andReturn(responseStatus);
        replay(response, requestAndResponse);
        boolean matches = new Status(expectedStatus).matches(requestAndResponse);
        verify(response, requestAndResponse);
        return matches;
    }

    @Test
    public void describeToTest() {
        Description description = mock(Description.class);
        expect(description.appendText(eq("Response status matching "))).andReturn(description);
        expect(description.appendValue(eq(DEFAULT_EXPECTED_VALUE))).andReturn(description);
        replay(description);
        new Status(DEFAULT_EXPECTED_VALUE).describeTo(description);
        verify(description);
    }

    @Test
    public void describeMismatchTest() {
        Description description = mock(Description.class);
        expect(description.appendText(eq("was "))).andReturn(description);
        expect(description.appendValue(eq(OK.getStatusCode()))).andReturn(description);
        expect(response.getStatus()).andReturn(OK.getStatusCode());
        replay(description, response, requestAndResponse);
        new Status(DEFAULT_EXPECTED_VALUE).describeMismatch(requestAndResponse, description);
        verify(description, response, requestAndResponse);
    }
}
