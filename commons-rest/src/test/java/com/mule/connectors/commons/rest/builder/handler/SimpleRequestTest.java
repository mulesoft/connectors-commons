package com.mule.connectors.commons.rest.builder.handler;

import com.mule.connectors.commons.rest.builder.request.Method;
import com.mule.connectors.commons.rest.builder.request.SimpleRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleRequestTest {

    private static final String DEFAULT_KEY = "key";
    private static final String DEFAULT_VALUE = "value";
    private SimpleRequest simpleRequest;

    @Before
    public void setup() {
        this.simpleRequest = new SimpleRequest(Method.GET);
    }

    @Test
    public void testAddQueryParam() {
        Map<String, String> mock = mock(Map.class);
        simpleRequest.setQueryParams(mock);
        expect(mock.put(same(DEFAULT_KEY), eq(DEFAULT_VALUE))).andReturn(null);
        replay(mock);
        this.simpleRequest.addQueryParam(DEFAULT_KEY, DEFAULT_VALUE);
        verify(mock);
    }

    @Test
    public void testAddHeader() {
        Map<String, String> mock = mock(Map.class);
        simpleRequest.setHeaders(mock);
        expect(mock.put(same(DEFAULT_KEY), eq(DEFAULT_VALUE))).andReturn(null);
        replay(mock);
        this.simpleRequest.addHeader(DEFAULT_KEY, DEFAULT_VALUE);
        verify(mock);
    }

    @Test
    public void testAddPathParam() {
        Map<String, String> mock = mock(Map.class);
        simpleRequest.setPathParams(mock);
        expect(mock.put(same(DEFAULT_KEY), eq(DEFAULT_VALUE))).andReturn(null);
        replay(mock);
        this.simpleRequest.addPathParam(DEFAULT_KEY, DEFAULT_VALUE);
        verify(mock);
    }

    /**
     * This test is made so that all the getter methods on SimpleRequest are used. These methods are for external use and as such this is the only way to test them.
     */
    @Test
    public void testGettersAndSetters() {
        String accept = "Accept";
        simpleRequest.setAccept(accept);
        assertThat(simpleRequest.getAccept(), is(accept));
        String path = "path";
        simpleRequest.setPath(path);
        assertThat(simpleRequest.getPath(), is(path));
        Map<String, String> pathParams = new HashMap<>();
        simpleRequest.setPathParams(pathParams);
        assertThat(simpleRequest.getPathParams(), is(pathParams));
        Object entity = "entity";
        simpleRequest.setEntity(entity);
        assertThat(simpleRequest.getEntity(), is(entity));
        Map<String, String> headers = new HashMap<>();
        simpleRequest.setHeaders(headers);
        assertThat(simpleRequest.getHeaders(), is(headers));
        String contentType = "contentType";
        simpleRequest.setContentType(contentType);
        assertThat(simpleRequest.getContentType(), is(contentType));
        Map<String, String> queryParams = new HashMap<>();
        simpleRequest.setQueryParams(queryParams);
        assertThat(simpleRequest.getQueryParams(), is(queryParams));
    }
}