package com.mulesoft.extensions.request.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.extensions.request.builder.exception.RequestEntityParsingException;
import com.mulesoft.extensions.request.builder.handler.DefaultResponseHandler;
import com.mulesoft.extensions.request.builder.handler.JacksonResponseHandler;
import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.listener.RequestListener;
import com.mulesoft.extensions.request.builder.parser.ParsingFunction;
import com.mulesoft.extensions.request.builder.request.Method;
import com.mulesoft.extensions.request.builder.request.SimpleRequest;
import com.mulesoft.extensions.request.builder.util.SimpleParameterizedType;
import org.apache.commons.text.StrSubstitutor;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.api.util.Preconditions;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.message.HttpMessageBuilder;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.mulesoft.extensions.request.builder.request.Method.DELETE;
import static com.mulesoft.extensions.request.builder.request.Method.GET;
import static com.mulesoft.extensions.request.builder.request.Method.HEAD;
import static com.mulesoft.extensions.request.builder.request.Method.OPTIONS;
import static com.mulesoft.extensions.request.builder.request.Method.PATCH;
import static com.mulesoft.extensions.request.builder.request.Method.POST;
import static com.mulesoft.extensions.request.builder.request.Method.PUT;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.function.Predicate.isEqual;

/**
 * Builder class for http requests.<br>
 * To use the requests do the following:<br>
 * <p>
 * <pre>
 * // GET request.
 * RequestBuilder.get(client, &quot;http://mypath.com/endpoint/action&quot;).execute();
 * // POST request.
 * RequestBuilder.post(client, &quot;http://mypath.com/endpoint/action&quot;).execute();
 * // PUT request.
 * RequestBuilder.put(client, &quot;http://mypath.com/endpoint/action&quot;).execute();
 * // DELETE request.
 * RequestBuilder.delete(client, &quot;http://mypath.com/endpoint/action&quot;).execute();
 * </pre>
 * <p>
 * The static methods generate an instance of this builder and allow the following:
 * <ul>
 * <li>Addition of query parameters by using the {@link RequestBuilder#queryParam(String, Object)} or through a {@link MultiMap}.</li>
 * <li>Addition of path parameters by setting them in the path parameter on the static method and assigning them to a placeholder and then setting them using
 * {@link RequestBuilder#pathParam(String, Object)}.</li>
 * <li>Setting an entity object to be sent as part of the request using {@link RequestBuilder#entity(Object)} for the object and {@link RequestBuilder#contentType(String)} for the
 * content type (default is APPLICATION_XML).</li>
 * <li>Handling the {@link HttpResponse} object using a {@link ResponseHandler}.</li>
 * <li>Setting the response type of the request by using the {@link RequestBuilder#responseType(Class, Type...)} </li>
 * <li>Setting headers using {@link RequestBuilder#header(String, Object)}.</li>
 * </ul>
 *
 * @param <T> The type response.
 * @author gaston.ortiz@mulesoft.com
 */
public class RequestBuilder<T> extends HttpMessageBuilder<RequestBuilder<T>, HttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(RequestBuilder.class);
    private final HttpClient client;
    private final Method method;
    private final String path;
    private final Map<String, String> pathParams = new HashMap<>();
    private final MultiMap<String, String> queryParams = new MultiMap<>();
    private HttpAuthentication authentication;
    private ResponseHandler<T> responseHandler;
    private List<RequestListener> requestListeners = new ArrayList<>();
    private boolean followRedirects;
    private int timeout;

    private RequestBuilder(HttpClient client, Method method, String path, ResponseHandler<T> responseHandler) {
        this.client = client;
        this.method = method;
        this.path = path;
        this.responseHandler = responseHandler;
    }

    public RequestBuilder<T> responseType(Class<?> baseType, Type... parameterTypes) {
        return responseType(new SimpleParameterizedType(baseType, parameterTypes));
    }

    public RequestBuilder<T> responseType(ParameterizedType parameterizedType) {
        this.responseHandler = new JacksonResponseHandler<>(parameterizedType);
        return this;
    }


    public RequestBuilder<T> responseHandler(ResponseHandler<T> responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    public RequestBuilder<T> header(String key, Object value) {
        Optional.ofNullable(value).map(Object::toString).filter(isEqual("").negate()).ifPresent(stringValue -> headers.put(key, stringValue));
        return this;
    }

    public RequestBuilder<T> authentication(HttpAuthentication authentication) {
        this.authentication = authentication;
        return this;
    }

    public RequestBuilder<T> basicAuthentication(String username, String password) {
        return authentication(HttpAuthentication.basic(username, password).build());
    }

    public RequestBuilder<T> ntlmAuthentication(String username, String password) {
        return authentication(HttpAuthentication.ntlm(username, password).build());
    }

    public RequestBuilder<T> ntlmAuthentication(String username, String password, String domain) {
        return ntlmAuthentication(format("%s/%s", domain, username), password);
    }

    public RequestBuilder<T> queryParam(String key, Object value) {
        Optional.ofNullable(value).map(Object::toString).filter(isEqual("").negate()).ifPresent(stringValue -> queryParams.put(key, stringValue));
        return this;
    }

    public RequestBuilder<T> queryParams(MultiMap<String, String> queryParams) {
        this.queryParams.putAll(queryParams);
        return this;
    }

    public RequestBuilder<T> pathParam(String key, Object value) {
        Optional.ofNullable(value).map(Object::toString).filter(isEqual("").negate()).ifPresent(stringValue -> pathParams.put(key, stringValue));
        return this;
    }

    public RequestBuilder<T> pathParams(Map<String, String> pathParams) {
        Optional.ofNullable(pathParams).ifPresent(this.pathParams::putAll);
        return this;
    }

    public RequestBuilder<T> entity(Object entity) {
        return entity(entity, new ObjectMapper()::writeValueAsString);
    }

    public <I> RequestBuilder<T> entity(I entity, ParsingFunction<I, String> converter) {
        try {
            return entity(converter.parse(entity));
        } catch (Exception e) {
            throw new RequestEntityParsingException(e);
        }
    }

    public RequestBuilder<T> entity(String entity) {
        this.entity = new ByteArrayHttpEntity(entity.getBytes(Charset.forName("UTF-8")));
        return this;
    }

    public RequestBuilder<T> followRedirects() {
        this.followRedirects = true;
        return this;
    }

    public RequestBuilder<T> doNotFollowRedirects() {
        this.followRedirects = false;
        return this;
    }

    public RequestBuilder<T> timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public RequestBuilder<T> accept(MediaType accept) {
        return Optional.ofNullable(accept)
                .map(Object::toString)
                .map(this::accept)
                .orElse(this);
    }

    public RequestBuilder<T> accept(String accept) {
        return header("Accept", accept);
    }

    public RequestBuilder<T> contentType(MediaType contentType) {
        return Optional.ofNullable(contentType)
                .map(Object::toString)
                .map(this::contentType)
                .orElse(this);
    }

    public RequestBuilder<T> contentType(String contentType) {
        return header("Content-Type", contentType);
    }

    public RequestBuilder<T> onBeforeRequest(RequestListener... listeners) {
        requestListeners.addAll(asList(listeners));
        return this;
    }

    public static <T> RequestBuilder<T> get(HttpClient client, String path, ResponseHandler<T> responseHandler) {
        return new RequestBuilder<>(client, GET, path, responseHandler);
    }

    public static RequestBuilder<String> get(HttpClient client, String path) {
        return get(client, path, new DefaultResponseHandler());
    }

    public static <T> RequestBuilder<T> post(HttpClient client, String path, ResponseHandler<T> responseHandler) {
        return new RequestBuilder<>(client, POST, path, responseHandler);
    }

    public static RequestBuilder<String> post(HttpClient client, String path) {
        return post(client, path, new DefaultResponseHandler());
    }

    public static <T> RequestBuilder<T> put(HttpClient client, String path, ResponseHandler<T> responseHandler) {
        return new RequestBuilder<>(client, PUT, path, responseHandler);
    }

    public static RequestBuilder<String> put(HttpClient client, String path) {
        return put(client, path, new DefaultResponseHandler());
    }

    public static <T> RequestBuilder<T> delete(HttpClient client, String path, ResponseHandler<T> responseHandler) {
        return new RequestBuilder<>(client, DELETE, path, responseHandler);
    }

    public static RequestBuilder<String> delete(HttpClient client, String path) {
        return delete(client, path, new DefaultResponseHandler());
    }

    public static <T> RequestBuilder<T> patch(HttpClient client, String path, ResponseHandler<T> responseHandler) {
        return new RequestBuilder<>(client, PATCH, path, responseHandler);
    }

    public static RequestBuilder<String> patch(HttpClient client, String path) {
        return patch(client, path, new DefaultResponseHandler());
    }

    public static <T> RequestBuilder<T> head(HttpClient client, String path, ResponseHandler<T> responseHandler) {
        return new RequestBuilder<>(client, HEAD, path, responseHandler);
    }

    public static RequestBuilder<String> head(HttpClient client, String path) {
        return head(client, path, new DefaultResponseHandler());
    }

    public static <T> RequestBuilder<T> options(HttpClient client, String path, ResponseHandler<T> responseHandler) {
        return new RequestBuilder<>(client, OPTIONS, path, responseHandler);
    }

    public static RequestBuilder<String> options(HttpClient client, String path) {
        return options(client, path, new DefaultResponseHandler());
    }

    @Override
    public SimpleRequest build() {
        Preconditions.checkNotNull(path, "URI must be specified to create an HTTP request");
        return new SimpleRequest(new StrSubstitutor(pathParams).replace(path), method, headers, queryParams, entity);
    }

    public T execute() throws IOException, TimeoutException {
        SimpleRequest request = build();
        for (RequestListener listener : requestListeners) {
            logger.debug("Request Listener {} found. Providing request.", listener.getClass());
            listener.handle(request);
        }
        HttpResponse response = client.send(build(), timeout, followRedirects, authentication);
        logger.debug("Parsing response.");
        return responseHandler.handleResponse(response);
    }

    public void execute(Consumer<T> onComplete) {
        SimpleRequest request = build();
        for (RequestListener listener : requestListeners) {
            logger.debug("Request Listener {} found. Providing request.", listener.getClass());
            listener.handle(request);
        }
        this.client.sendAsync(request, timeout, followRedirects, authentication).whenCompleteAsync((response, throwable) -> {
            logger.debug("Parsing response.");
            onComplete.accept(responseHandler.handleResponse(response));
        });
    }
}
