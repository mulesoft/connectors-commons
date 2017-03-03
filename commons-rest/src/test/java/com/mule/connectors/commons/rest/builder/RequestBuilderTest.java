package com.mule.connectors.commons.rest.builder;

import com.google.common.collect.ImmutableMap;
import com.mule.connectors.commons.rest.builder.handler.ResponseHandler;
import com.mule.connectors.commons.rest.builder.listener.RequestListener;
import com.mule.connectors.commons.rest.builder.util.SimpleParameterizedType;
import org.easymock.EasyMock;
import org.glassfish.hk2.utilities.reflection.ParameterizedTypeImpl;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.nullToEmpty;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

public class RequestBuilderTest {

    private Validator validator;

    @Before
    public void before() {
        this.validator = new Validator();
    }

    @Test
    public void testSimpleGetExecute() {
        validator.validateGet();
    }

    @Test
    public void testSimplePostExecute() {
        validator.validatePost();
    }

    @Test
    public void testSimplePutExecute() {
        validator.validatePut();
    }

    @Test
    public void testSimpleDeleteExecute() {
        validator.validateDelete();
    }

    @Test
    public void testDifferentPath() {
        validator.path = "/root";
        validator.validateGet();
    }

    @Test
    public void testDifferentPathParams() {
        validator.pathParams.put("test", "test");
        validator.pathParams.put("test2", "test2");
        validator.pathParams.put("test3", null);
        validator.pathParams.put("test4", "");
        validator.validateGet();
    }

    @Test
    public void testDifferentQueryParams() {
        validator.queryParams.putSingle("test", "test");
        validator.queryParams.putSingle("test2", "test2");
        validator.queryParams.put("test3", null);
        validator.queryParams.putSingle("test4", null);
        validator.queryParams.putSingle("test5", "");
        validator.validateGet();
    }

    @Test
    public void testDifferentHeaders() {
        validator.headers.put("test", "test");
        validator.headers.put("test2", null);
        validator.headers.put("test3", "");
        validator.validateGet();
    }

    @Test
    public void testBasicAuthorization() {
        validator.username = "userName";
        validator.password = "password";
        validator.validateGet();
    }

    @Test
    public void testBasicAuthorizationNoUsername() {
        validator.password = "password";
        validator.validateGet();
    }

    @Test
    public void testBasicAuthorizationNoPassword() {
        validator.username = "userName";
        validator.validateGet();
    }

    @Test
    public void testDifferentContentType() {
        validator.contentType = MediaType.APPLICATION_JSON;
        validator.validateGet();
    }

    @Test
    public void testDifferentResponseType() {
        validator.responseType = String.class;
        validator.validateGet();
    }

    @Test
    public void testTypedResponseType() {
        validator.responseType = new ParameterizedTypeImpl(Map.class, String.class, Object.class);
        validator.validateGet();
    }

    @Test
    public void testGetEntity() {
        validator.entity = ImmutableMap.builder().put("test", "test").put("test2", "test2").build();
        validator.validateGet();
    }

    @Test
    public void testPostEntity() {
        validator.entity = ImmutableMap.builder().put("test", "test").put("test2", "test2").build();
        validator.validatePost();
    }

    @Test
    public void testPutEntity() {
        validator.entity = ImmutableMap.builder().put("test", "test").put("test2", "test2").build();
        validator.validatePost();
    }

    @Test
    public void testRequestListener() {
        validator.entity = ImmutableMap.builder().put("test", "test").put("test2", "test2").build();
        validator.listeners = new RequestListener[] { new DummyRequestListener() };
        validator.validatePost();
    }

    private final class Validator {
        private static final String DEFAULT_PATH = "/default";
        private RequestListener[] listeners = new RequestListener[] {};
        private Invocation.Builder invocationBuilder;
        private WebTarget target;
        private Client client;
        private Response response;
        private ResponseHandler<?> responseHandler;
        private String path;
        private Map<String, String> pathParams;
        private MultivaluedMap<String, String> queryParams;
        private Map<String, Object> headers;
        private String username;
        private String password;
        private Object entity;
        private String contentType;
        private String accept;
        private Type responseType;

        public Validator() {
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
            contentType = APPLICATION_XML;
            accept = APPLICATION_XML;
        }

        public void validateGet() {
            expect(invocationBuilder.get()).andReturn(response);
            validate(RequestBuilder.get(client, path));
        }

        public void validatePost() {
            expect(invocationBuilder.post(anyObject(Entity.class))).andReturn(response);
            validate(RequestBuilder.post(client, path));
        }

        public void validatePut() {
            expect(invocationBuilder.put(EasyMock.anyObject(Entity.class))).andReturn(response);
            validate(RequestBuilder.put(client, path));
        }

        public void validateDelete() {
            expect(invocationBuilder.delete()).andReturn(response);
            validate(RequestBuilder.delete(client, path));
        }

        private void validate(RequestBuilder requestBuilder) {
            requestBuilder.accept(accept);
            expect(invocationBuilder.accept(eq(accept))).andReturn(invocationBuilder);
            expect(target.path(eq(path))).andReturn(target);
            expect(target.getUri()).andReturn(URI.create(path));
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                requestBuilder.pathParam(entry.getKey(), entry.getValue());
            }
            expect(target.resolveTemplates(anyObject(Map.class))).andReturn(target);
            requestBuilder.queryParams(queryParams);
            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty() && !nullToEmpty(entry.getValue().get(0)).isEmpty()) {
                    expect(target.queryParam(eq(entry.getKey()), eq(entry.getValue().get(0)))).andReturn(target);
                }
            }
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
                if (entry.getValue() != null && !nullToEmpty(entry.getValue().toString()).isEmpty()) {
                    expect(invocationBuilder.header(eq(entry.getKey()), eq(entry.getValue()))).andReturn(invocationBuilder);
                }
            }
            if (username != null || password != null) {
                requestBuilder.basicAuthorization(username, password);
                expect(invocationBuilder.header(eq("Authorization"), eq(format("Basic %s", printBase64Binary(format("%s:%s", username, password).getBytes()))))).andReturn(
                        invocationBuilder);
            }
            requestBuilder.entity(entity);
            requestBuilder.contentType(contentType);
            requestBuilder.responseType(responseType);
            requestBuilder.responseHandler(responseHandler);
            requestBuilder.onBeforeRequest(listeners);
            expect(responseHandler.handleResponse(eq(response), eq(new SimpleParameterizedType(responseType)))).andReturn(null);
            expect(response.bufferEntity()).andReturn(TRUE);
            replay(client, target, invocationBuilder, responseHandler, response);
            requestBuilder.execute();
        }
    }
}