package com.mule.connectors.commons.rest.test.assertion.body;

import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EqualsTest {
    private static final String DEFAULT_BODY = "This is the body.";
    private Response response;
    private RequestAndResponse requestAndResponse;

    @Before
    public void setup () {
        response = mock(Response.class);
        requestAndResponse = mock(RequestAndResponse.class);
        expect(requestAndResponse.getResponse()).andReturn(response);
    }

    @Test
    public void validateEquals() {
        assertThat(match(DEFAULT_BODY, DEFAULT_BODY), is(true));
    }

    @Test
    public void validatePartialMatch() {
        assertThat(match(DEFAULT_BODY, "body"), is(false));
    }

    @Test
    public void validateNotContains() {
        assertThat(match(DEFAULT_BODY, "Nope"), is(false));
    }

    private boolean match(String body, String expectedValue) {
        expect(response.readEntity(eq(String.class))).andReturn(body);
        replay(response, requestAndResponse);
        boolean matches = new Equals(expectedValue).matches(requestAndResponse);
        verify(response, requestAndResponse);
        return matches;
    }

    @Test
    public void describeToTest() {
        Description description = mock(Description.class);
        expect(description.appendText(eq("SimpleRequest body is "))).andReturn(description);
        expect(description.appendValue(eq(DEFAULT_BODY))).andReturn(description);
        replay(description);
        new Equals(DEFAULT_BODY).describeTo(description);
        verify(description);
    }

    @Test
    public void describeMismatchTest() {
        Description description = mock(Description.class);
        expect(description.appendText(eq("was "))).andReturn(description);
        expect(description.appendValue(eq(DEFAULT_BODY))).andReturn(description);
        expect(response.readEntity(eq(String.class))).andReturn(DEFAULT_BODY);
        replay(description, response, requestAndResponse);
        new Equals(DEFAULT_BODY).describeMismatch(requestAndResponse, description);
        verify(description, response, requestAndResponse);
    }

}
