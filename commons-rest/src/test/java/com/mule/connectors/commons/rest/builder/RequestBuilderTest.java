package com.mule.connectors.commons.rest.builder;

import com.google.common.collect.ImmutableMap;
import com.mule.connectors.commons.rest.builder.handler.ResponseHandler;
import org.apache.commons.lang3.StringUtils;
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
import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;

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
        validator.pathParams = ImmutableMap.<String, Object>builder().put("test", "test").put("test2", "test2").build();
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
        validator.entity = ImmutableMap.builder().put("test", "test").put("test2", "test2");
        validator.validateGet();
    }

    @Test
    public void testPostEntity() {
        validator.entity = ImmutableMap.builder().put("test", "test").put("test2", "test2");
        validator.validatePost();
    }

    @Test
    public void testPutEntity() {
        validator.entity = ImmutableMap.builder().put("test", "test").put("test2", "test2");
        validator.validatePost();
    }

    private final class Validator {

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
            contentType = MediaType.APPLICATION_XML;
            accept = MediaType.APPLICATION_XML;
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
            if (username != null || password != null) {
                requestBuilder.basicAuthorization(username, password);
                expect(invocationBuilder.header(eq("Authorization"),
                        eq(String.format("Basic %s", DatatypeConverter.printBase64Binary(String.format("%s:%s", username, password).getBytes()))))).andReturn(
                        invocationBuilder);
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
}