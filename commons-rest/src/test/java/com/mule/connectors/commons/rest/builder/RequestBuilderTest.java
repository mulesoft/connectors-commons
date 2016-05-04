package com.mule.connectors.commons.rest.builder;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.mule.connectors.commons.rest.builder.handler.ResponseHandler;
import org.apache.commons.lang3.StringUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class RequestBuilderTest {
    private static final String DEFAULT_PATH = "/default";
    private Invocation.Builder invocationBuilder;
    private WebTarget target;
    private Client client;
    private Response response;
    private ResponseHandler<?> responseHandler;
    private String path;
    private Map<String, Object> pathParams;
    private MultivaluedMap<String, String> queryParams;
    private Map<String, Object> headers;
    private Object entity;
    private String contentType;
    private String accept;
    private Class responseType;

    @Before
    public void before() {
        response = createMock(Response.class);
        invocationBuilder = createMock(Invocation.Builder.class);
        target = createMock(WebTarget.class);
        expect(target.request()).andReturn(invocationBuilder);
        client = createMock(Client.class);
        expect(client.target(eq(""))).andReturn(target);
        responseHandler = createMock(ResponseHandler.class);

        path = DEFAULT_PATH;
        pathParams = new HashMap<>();
        queryParams = new MultivaluedHashMap<>();
        headers = new HashMap<>();
        contentType = MediaType.APPLICATION_XML;
        accept = MediaType.APPLICATION_XML;
    }

    @Test
    public void testSimpleGetExecute() {
        testGetExecute();
    }

    @Test
    public void testSimplePostExecute() {
        testPostExecute();
    }

    @Test
    public void testSimplePutExecute() {
        testPutExecute();
    }

    @Test
    public void testSimpleDeleteExecute() {
        testDeleteExecute();
    }

    @Test
    public void testDifferentPath() {
        path = "/root";
        testGetExecute();
    }

    @Test
    public void testDifferentPathParams() {
        pathParams = ImmutableMap.<String, Object>builder().put("test", "test").put("test2", "test2").build();
        testGetExecute();
    }

    @Test
    public void testDifferentQueryParams() {
        queryParams.putSingle("test", "test");
        queryParams.putSingle("test2", "test2");
        queryParams.put("test3", null);
        queryParams.putSingle("test4", null);
        queryParams.putSingle("test5", "");
        testGetExecute();
    }

    @Test
    public void testDifferentHeaders() {
        headers.put("test", "test");
        headers.put("test2", null);
        headers.put("test3", "");
        testGetExecute();
    }

    @Test
    public void testDifferentContentType() {
        contentType = MediaType.APPLICATION_JSON;
        testGetExecute();
    }

    @Test
    public void testDifferentResponseType() {
        responseType = String.class;
        testGetExecute();
    }

    @Test
    public void testGetEntity() {
        entity = ImmutableMap.builder().put("test", "test").put("test2", "test2");
        testGetExecute();
    }

    @Test
    public void testPostEntity() {
        entity = ImmutableMap.builder().put("test", "test").put("test2", "test2");
        testPostExecute();
    }

    @Test
    public void testPutEntity() {
        entity = ImmutableMap.builder().put("test", "test").put("test2", "test2");
        testPostExecute();
    }

    private void testGetExecute() {
        expect(invocationBuilder.get()).andReturn(response);
        testExecute(RequestBuilder.get(client, path));
    }

    private void testPostExecute() {
        expect(invocationBuilder.post(anyObject(Entity.class))).andReturn(response);
        testExecute(RequestBuilder.post(client, path));
    }

    private void testPutExecute() {
        expect(invocationBuilder.put(EasyMock.anyObject(Entity.class))).andReturn(response);
        testExecute(RequestBuilder.put(client, path));
    }

    private void testDeleteExecute() {
        expect(invocationBuilder.delete()).andReturn(response);
        testExecute(RequestBuilder.delete(client, path));
    }

    private void testExecute(RequestBuilder requestBuilder) {
        requestBuilder.accept(accept);
        expect(invocationBuilder.accept(eq(accept))).andReturn(invocationBuilder);
        expect(target.path(eq(path))).andReturn(target);
        expect(target.getUri()).andReturn(URI.create(path));
        for (Map.Entry<String, Object> entry : pathParams.entrySet()) {
            requestBuilder.pathParam(entry.getKey(), entry.getValue());
        }
        expect(target.resolveTemplates(eq(pathParams))).andReturn(target);
        requestBuilder.queryParams(queryParams);
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty() && StringUtils.isNotBlank(entry.getValue().get(0))) {
                expect(target.queryParam(eq(entry.getKey()), eq(entry.getValue().get(0)))).andReturn(target);
            }
        }
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
            if (entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())) {
                expect(invocationBuilder.header(eq(entry.getKey()), eq(entry.getValue()))).andReturn(invocationBuilder);
            }
        }
        requestBuilder.entity(entity);
        requestBuilder.contentType(contentType);
        requestBuilder.responseType(responseType);
        requestBuilder.responseHandler(responseHandler);
        expect(responseHandler.handleResponse(eq(response), eq(responseType))).andReturn(null);
        expect(response.bufferEntity()).andReturn(Boolean.TRUE);
        replay(client, target, invocationBuilder, responseHandler, response);
        requestBuilder.execute();
    }
}