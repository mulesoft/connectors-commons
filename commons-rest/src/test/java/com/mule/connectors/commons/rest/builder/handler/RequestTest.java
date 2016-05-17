package com.mule.connectors.commons.rest.builder.handler;

import com.mule.connectors.commons.rest.builder.request.GetRequest;
import com.mule.connectors.commons.rest.builder.request.Request;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.easymock.EasyMock.*;

public class RequestTest {

    private static final String DEFAULT_KEY = "key";
    private static final String DEFAULT_VALUE = "value";
    private Request request;

    @Before
    public void setup() {
        this.request = new GetRequest();
    }

    @Test
    public void testAddQueryParam() {
        Map<String, String> mock = mock(Map.class);
        request.setQueryParams(mock);
        expect(mock.put(same(DEFAULT_KEY), eq(DEFAULT_VALUE))).andReturn(null);
        replay(mock);
        this.request.addQueryParam(DEFAULT_KEY, DEFAULT_VALUE);
    }

    @Test
    public void testAddHeader() {
        Map<String, String> mock = mock(Map.class);
        request.setHeaders(mock);
        expect(mock.put(same(DEFAULT_KEY), eq(DEFAULT_VALUE))).andReturn(null);
        replay(mock);
        this.request.addHeader(DEFAULT_KEY, DEFAULT_VALUE);
    }

    @Test
    public void testAddPathParam() {
        Map<String, Object> mock = mock(Map.class);
        request.setPathParams(mock);
        expect(mock.put(same(DEFAULT_KEY), eq(DEFAULT_VALUE))).andReturn(null);
        replay(mock);
        this.request.addPathParam(DEFAULT_KEY, DEFAULT_VALUE);
    }
}