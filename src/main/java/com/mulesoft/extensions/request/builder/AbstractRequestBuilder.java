package com.mulesoft.extensions.request.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.extensions.request.builder.exception.RequestEntityParsingException;
import com.mulesoft.extensions.request.builder.handler.JacksonResponseHandler;
import com.mulesoft.extensions.request.builder.handler.ResponseHandler;
import com.mulesoft.extensions.request.builder.listener.RequestListener;
import com.mulesoft.extensions.request.builder.parser.ParsingFunction;
import com.mulesoft.extensions.request.builder.request.ExecutableRequestConstructor;
import com.mulesoft.extensions.request.builder.request.Method;
import com.mulesoft.extensions.request.builder.util.SimpleParameterizedType;
import org.apache.commons.text.StrSubstitutor;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.api.util.Preconditions;
import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.message.HttpMessageBuilder;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.function.Predicate.isEqual;

/**
 * Builder class for http requests.<br>
 * To use the requests do the following:<br>
 * <p>
 * <pre>
 * // GET request.
 * AbstractRequestBuilder.get(client, &quot;http://mypath.com/endpoint/action&quot;).execute();
 * // POST request.
 * AbstractRequestBuilder.post(client, &quot;http://mypath.com/endpoint/action&quot;).execute();
 * // PUT request.
 * AbstractRequestBuilder.put(client, &quot;http://mypath.com/endpoint/action&quot;).execute();
 * // DELETE request.
 * AbstractRequestBuilder.delete(client, &quot;http://mypath.com/endpoint/action&quot;).execute();
 * </pre>
 * <p>
 * The static methods generate an instance of this builder and allow the following:
 * <ul>
 * <li>Addition of query parameters by using the {@link AbstractRequestBuilder#queryParam(String, Object)} or through a {@link MultiMap}.</li>
 * <li>Addition of path parameters by setting them in the path parameter on the static method and assigning them to a placeholder and then setting them using
 * {@link AbstractRequestBuilder#pathParam(String, Object)}.</li>
 * <li>Setting an entity object to be sent as part of the request using {@link AbstractRequestBuilder#entity(Object)} for the object and {@link AbstractRequestBuilder#contentType(String)} for the
 * content type (default is APPLICATION_XML).</li>
 * <li>Handling the {@link HttpResponse} object using a {@link ResponseHandler}.</li>
 * <li>Setting the response type of the request by using the {@link AbstractRequestBuilder#responseType(Class, Type...)} </li>
 * <li>Setting headers using {@link AbstractRequestBuilder#header(String, Object)}.</li>
 * </ul>
 *
 * @param <RESULT> The type response.
 * @author gaston.ortiz@mulesoft.com
 */
public abstract class AbstractRequestBuilder<RESULT, TYPE extends HttpRequest, SELF extends AbstractRequestBuilder> extends HttpMessageBuilder<SELF, HttpRequest> implements RequestBuilder {

    private final ExecutableRequestConstructor<RESULT, TYPE> executableRequestConstructor;
    private final Method method;
    private final String path;
    private final Map<String, String> pathParams = new HashMap<>();
    private final MultiMap<String, String> queryParams = new MultiMap<>();
    private HttpAuthentication authentication;
    private ResponseHandler<RESULT> responseHandler;
    private List<RequestListener> requestListeners = new ArrayList<>();
    private boolean followRedirects;
    private int timeout;

    protected AbstractRequestBuilder(ExecutableRequestConstructor<RESULT, TYPE> executableRequestConstructor,
                                     Method method,
                                     String path,
                                     ResponseHandler<RESULT> responseHandler) {
        this.executableRequestConstructor = executableRequestConstructor;
        this.method = method;
        this.path = path;
        this.responseHandler = responseHandler;
    }

    public SELF responseType(Class<?> baseType, Type... parameterTypes) {
        return responseType(new SimpleParameterizedType(baseType, parameterTypes));
    }

    public SELF responseType(ParameterizedType parameterizedType) {
        this.responseHandler = new JacksonResponseHandler<>(parameterizedType);
        return (SELF) this;
    }

    public SELF responseHandler(ResponseHandler<RESULT> responseHandler) {
        this.responseHandler = responseHandler;
        return (SELF) this;
    }

    public SELF header(String key, Object value) {
        Optional.ofNullable(value).map(Object::toString).filter(isEqual("").negate()).ifPresent(stringValue -> headers.put(key, stringValue));
        return (SELF) this;
    }

    public SELF authentication(HttpAuthentication authentication) {
        this.authentication = authentication;
        return (SELF) this;
    }

    public SELF basicAuthentication(String username, String password) {
        return authentication(HttpAuthentication.basic(username, password).build());
    }

    public SELF ntlmAuthentication(String username, String password) {
        return authentication(HttpAuthentication.ntlm(username, password).build());
    }

    public SELF ntlmAuthentication(String username, String password, String domain) {
        return ntlmAuthentication(format("%s/%s", domain, username), password);
    }

    public SELF queryParam(String key, Object value) {
        Optional.ofNullable(value).map(Object::toString).filter(isEqual("").negate()).ifPresent(stringValue -> queryParams.put(key, stringValue));
        return (SELF) this;
    }

    public SELF queryParams(MultiMap<String, String> queryParams) {
        this.queryParams.putAll(queryParams);
        return (SELF) this;
    }

    public SELF pathParam(String key, Object value) {
        Optional.ofNullable(value).map(Object::toString).filter(isEqual("").negate()).ifPresent(stringValue -> pathParams.put(key, stringValue));
        return (SELF) this;
    }

    public SELF pathParams(Map<String, String> pathParams) {
        Optional.ofNullable(pathParams).ifPresent(this.pathParams::putAll);
        return (SELF) this;
    }

    public SELF entity(Object entity) {
        return entity(entity, new ObjectMapper()::writeValueAsString);
    }

    public <I> SELF entity(I entity, ParsingFunction<I, String> converter) {
        try {
            return entity(converter.parse(entity));
        } catch (Exception e) {
            throw new RequestEntityParsingException(e);
        }
    }

    public SELF entity(String entity) {
        this.entity = new ByteArrayHttpEntity(entity.getBytes(Charset.forName("UTF-8")));
        return (SELF) this;
    }

    public SELF followRedirects() {
        this.followRedirects = true;
        return (SELF) this;
    }

    public SELF doNotFollowRedirects() {
        this.followRedirects = false;
        return (SELF) this;
    }

    public SELF timeout(int timeout) {
        this.timeout = timeout;
        return (SELF) this;
    }

    public SELF accept(MediaType accept) {
        return Optional.ofNullable(accept)
                .map(Object::toString)
                .map(this::accept)
                .orElse((SELF) this);
    }

    public SELF accept(String accept) {
        return header("Accept", accept);
    }

    public SELF contentType(MediaType contentType) {
        return Optional.ofNullable(contentType)
                .map(Object::toString)
                .map(this::contentType)
                .orElse((SELF) this);
    }

    public SELF contentType(String contentType) {
        return header("Content-Type", contentType);
    }

    public SELF onBeforeRequest(RequestListener... listeners) {
        requestListeners.addAll(asList(listeners));
        return (SELF) this;
    }

    @Override
    public TYPE build() {
        Preconditions.checkNotNull(path, "URI must be specified to create an HTTP request");
        return executableRequestConstructor.create(new StrSubstitutor(pathParams).replace(path), method, authentication, headers, queryParams, entity, timeout, followRedirects, requestListeners, responseHandler);
    }
}
