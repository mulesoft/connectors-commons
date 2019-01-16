package com.mulesoft.extensions.request.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.extensions.request.builder.exception.*;
import com.mulesoft.extensions.request.builder.handler.DefaultResponseHandler;
import com.mulesoft.extensions.request.builder.handler.JacksonResponseHandler;
import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.listener.RequestListener;
import com.mulesoft.extensions.request.builder.request.Method;
import com.mulesoft.extensions.request.builder.util.MockResponseCallback;
import com.mulesoft.extensions.request.builder.util.SimpleParameterizedType;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.entity.EmptyHttpEntity;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mulesoft.extensions.request.builder.request.Method.*;
import static java.util.function.Function.identity;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.fail;

public class AsyncRequestBuilderTest {
    private static final Map<String, String> DEFAULT_MAP = Stream.of("test", "test2").collect(Collectors.toMap(identity(), identity()));
    private static final String ACCEPT = "accept";
    private static final String CONTENT_TYPE = "content-type";
    private final HttpEntity DEFAULT_CONTENT;

    public AsyncRequestBuilderTest() {
        try {
            DEFAULT_CONTENT = new ByteArrayHttpEntity(new ObjectMapper().writeValueAsString(DEFAULT_MAP).getBytes(Charset.forName("UTF-8")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

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
    public void testSimplePatchExecute() {
        validator.validatePatch();
    }

    @Test
    public void testSimpleHeadExecute() {
        validator.validateHead();
    }

    @Test
    public void testSimpleOptionsExecute() {
        validator.validateOptions();
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
        validator.queryParams.put("test", "test");
        validator.queryParams.put("test2", "test2");
        validator.queryParams.put("test3", (String) null);
        validator.queryParams.put("test4", (String) null);
        validator.queryParams.put("test5", "");
        validator.validateGet();
    }

    @Test
    public void testDifferentHeaders() {
        validator.headers.put("test", "test");
        validator.headers.put("test3", "");
        validator.validateGet();
    }

    @Test
    public void testBasicAuthorization() {
        validator.authentication = HttpAuthentication.basic("userName", "password").build();
        validator.validateGet();
    }

    @Test
    public void testBasicAuthorizationEmptyUsername() {
        validator.authentication = HttpAuthentication.basic("", "password").build();
        validator.validateGet();
    }

    @Test
    public void testBasicAuthorizationEmptyPassword() {
        validator.authentication = HttpAuthentication.basic("userName", "").build();
        validator.validateGet();
    }

    @Test
    public void testDifferentContentType() {
        validator.contentType = MediaType.APPLICATION_JSON;
        validator.validateGet();
    }

    @Test
    public void testDifferentAccept() {
        validator.accept = MediaType.APPLICATION_JSON;
        validator.validateGet();
    }

    @Test
    public void testDifferentResponseType() {
        validator.responseType = new SimpleParameterizedType(String.class);
        validator.responseHandler = new JacksonResponseHandler<String>(String.class);
        validator.validateGet();
    }

    @Test
    public void testTypedResponseType() {
        validator.responseType = new SimpleParameterizedType(Map.class, String.class, Object.class);
        validator.responseHandler = new JacksonResponseHandler<Map<String, Object>>(validator.responseType);
        validator.validateGet();
    }

    @Test
    public void testGetEntity() {
        validator.requestEntity = DEFAULT_CONTENT;
        validator.validateGet();
    }

    @Test
    public void testPostEntity() {
        validator.requestEntity = DEFAULT_CONTENT;
        validator.validatePost();
    }

    @Test
    public void testPutEntity() {
        validator.requestEntity = DEFAULT_CONTENT;
        validator.validatePut();
    }

    @Test
    public void testPatchEntity() {
        validator.requestEntity = DEFAULT_CONTENT;
        validator.validatePatch();
    }

    @Test
    public void testRequestListener() {
        validator.requestEntity = DEFAULT_CONTENT;
        validator.listeners = new RequestListener[]{new DummyRequestListener()};
        validator.validatePost();
    }

    @Test
    public void testErrorCodeOver299() {
        validator.responseStatusCode = 404;
        validator.validateGet();
        validator.expectedException = RequestFailedException.class;
    }

    @Test
    public void testErrorCodeBelow200() {
        validator.responseStatusCode = 199;
        validator.validateGet();
        validator.expectedException = RequestFailedException.class;
    }

    @Test
    public void testErrorParsingResponse() throws IOException {
        validator.responseEntity = EasyMock.mock(HttpEntity.class);
        InputStream content = EasyMock.mock(InputStream.class);
        expect(content.read(anyObject(new byte[]{}.getClass()), anyInt(), anyInt())).andThrow(new IOException("Mock failure."));
        expect(validator.responseEntity.getContent()).andReturn(content).anyTimes();
        replay(content, validator.responseEntity);
        validator.expectedException = ResponseEntityParsingException.class;
        validator.validateGet();
    }

    @Test(expected = RequestEntityParsingException.class)
    public void testErrorParsingRequest() {
        RequestBuilder.get("").entity("", input -> {
            throw new RuntimeException("Fail.");
        });
    }

    @Test
    public void testRequestTimeout() {
        validator.exceptionThrown = new TimeoutException();
        validator.expectedException = RequestTimeoutException.class;
        validator.validateGet();
    }

    @Test
    public void testConnectionException() {
        validator.exceptionThrown = new IOException();
        validator.expectedException = RequestConnectionException.class;
        validator.validateGet();
    }

    private final class Validator {
        private static final String DEFAULT_PATH = "/default";
        public Class<? extends Throwable> expectedException;
        private HttpClient client;
        private HttpAuthentication authentication;
        private RequestListener[] listeners = new RequestListener[]{};
        private HttpResponse response;
        private int responseStatusCode = 200;
        private ResponseHandler<?> responseHandler = new DefaultResponseHandler();
        private String path;
        private Map<String, String> pathParams;
        private MultiMap<String, String> queryParams;
        private MultiMap<String, String> headers = new MultiMap<>();
        private Method method;
        private HttpEntity requestEntity = new EmptyHttpEntity();
        private HttpEntity responseEntity = new EmptyHttpEntity();
        private MediaType contentType;
        private MediaType accept;
        private ParameterizedType responseType;
        private Throwable exceptionThrown;

        public Validator() {
            response = createMock(HttpResponse.class);
            client = createMock(HttpClient.class);

            path = DEFAULT_PATH;
            pathParams = new HashMap<>();
            queryParams = new MultiMap<>();
        }

        public void validateGet() {
            method = GET;
            validate(RequestBuilder.asyncGet(path));
        }

        public void validatePost() {
            method = POST;
            validate(RequestBuilder.asyncPost(path));
        }

        public void validatePut() {
            method = PUT;
            validate(RequestBuilder.asyncPut(path));
        }

        public void validateDelete() {
            method = DELETE;
            validate(RequestBuilder.asyncDelete(path));
        }

        public void validatePatch() {
            method = PATCH;
            validate(RequestBuilder.asyncPatch(path));
        }

        public void validateHead() {
            method = HEAD;
            validate(RequestBuilder.asyncHead(path));
        }

        public void validateOptions() {
            method = OPTIONS;
            validate(RequestBuilder.asyncOptions(path));
        }

        private <T> void validate(AsyncRequestBuilder<T> requestBuilder) {
            try {
                Optional.ofNullable(responseType).ifPresent(requestBuilder::responseType);
                HttpRequest build = requestBuilder
                        .authentication(authentication)
                        .headers(headers)
                        .onBeforeRequest(listeners)
                        .entity(requestEntity)
                        .pathParams(pathParams)
                        .queryParams(queryParams)
                        .contentType(contentType)
                        .accept(accept)
                        .build();
                Optional.ofNullable(contentType).map(Object::toString).ifPresent(value -> headers.put(CONTENT_TYPE, value));
                Optional.ofNullable(accept).map(Object::toString).ifPresent(value -> headers.put(ACCEPT, value));
                assertThat(build.getHeaderValue(ACCEPT), equalTo(Optional.ofNullable(accept).map(Object::toString).orElse(null)));
                assertThat(build.getHeaderValue(CONTENT_TYPE), equalTo(Optional.ofNullable(contentType).map(Object::toString).orElse(null)));
                headers.entryList().stream().forEach(entry -> assertThat(build.getHeaderValues(entry.getKey()).size(), greaterThan(0)));
                assertThat(build.getMethod(), equalTo(method.toString()));
                assertThat(build.getPath(), equalTo(path));
                assertThat(build.getProtocol(), nullValue());
                assertThat(build.getQueryParams(), equalTo(queryParams));
                assertThat(build.getUri(), equalTo(URI.create(path)));
                assertThat(build.getEntity().getBytes(), equalTo(requestEntity.getBytes()));
                assertThat(build.getHeaderNames(), equalTo(headers.keySet()));
                assertThat(build.getHeaders().entryList(), equalTo(headers.entryList()));
                CompletableFuture<HttpResponse> completableFuture = new CompletableFuture<>();
                expect(client.sendAsync(anyObject(HttpRequest.class), anyInt(), anyBoolean(), eq(authentication))).andReturn(completableFuture).anyTimes();
                expect(response.getStatusCode()).andReturn(responseStatusCode).anyTimes();
                expect(response.getEntity()).andReturn(responseEntity).anyTimes();
                replay(client, response);
                requestBuilder.build().execute(client, new MockResponseCallback(responseHandler, response, expectedException));
                Optional.ofNullable(exceptionThrown).ifPresent(completableFuture::completeExceptionally);
                Optional.ofNullable(response).filter(response -> Objects.isNull(expectedException)).ifPresent(completableFuture::complete);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}