package com.mule.connectors.commons.rest.test.assertion.header;

import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HeaderContainsTest {
    private static final String DEFAULT_HEADER_KEY = "key";
    private Response response;
    private RequestAndResponse requestAndResponse;

    @Before
    public void setup () {
        response = mock(Response.class);
        requestAndResponse = mock(RequestAndResponse.class);
        expect(requestAndResponse.getResponse()).andReturn(response);
    }

    @Test
    public void validateContains() {
        assertThat(match(DEFAULT_HEADER_KEY, DEFAULT_HEADER_KEY), is(true));
    }

    @Test
    public void validateNotContains() {
        assertThat(match(DEFAULT_HEADER_KEY, "Nope"), is(false));
    }

    private boolean match(String key, String expectedValue) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle(key, "test");
        expect(response.getHeaders()).andReturn(headers);
        replay(response, requestAndResponse);
        boolean matches = new HeaderContains(expectedValue).matches(requestAndResponse);
        verify(response, requestAndResponse);
        return matches;
    }

    @Test
    public void describeToTest() {
        Description description = mock(Description.class);
        expect(description.appendText(eq("Existing header "))).andReturn(description);
        expect(description.appendValue(eq(DEFAULT_HEADER_KEY))).andReturn(description);
        replay(description);
        new HeaderContains(DEFAULT_HEADER_KEY).describeTo(description);
        verify(description);
    }

    @Test
    public void describeMismatchTest() {
        requestAndResponse = mock(RequestAndResponse.class);
        Description description = mock(Description.class);
        expect(description.appendText(eq(" not found."))).andReturn(description);
        replay(description, requestAndResponse);
        new HeaderContains(DEFAULT_HEADER_KEY).describeMismatch(requestAndResponse, description);
        verify(description, requestAndResponse);
    }
}
