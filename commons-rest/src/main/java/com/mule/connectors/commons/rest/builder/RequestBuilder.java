package com.mule.connectors.commons.rest.builder;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.mule.connectors.commons.rest.builder.handler.DefaultXMLResponseHandler;
import com.mule.connectors.commons.rest.builder.handler.ResponseHandler;
import com.mule.connectors.commons.rest.builder.strategy.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;

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
 * <li>Addition of query parameters by using the {@link RequestBuilder#queryParam(String, Object)} or through a {@link MultivaluedMap}.</li>
 * <li>Addition of path parameters by setting them in the path parameter on the static method and assigning them to a placeholder and then setting them using
 * {@link RequestBuilder#pathParam(String, Object)}.</li>
 * <li>Setting an entity object to be sent as part of the request using {@link RequestBuilder#entity(Object)} for the object and {@link RequestBuilder#contentType(String)} for the
 * content type (default is APPLICATION_XML).</li>
 * <li>Handling the {@link Response} object using a {@link ResponseHandler}.</li>
 * <li>Setting headers using {@link RequestBuilder#header(String, Object)}.</li>
 * </ul>
 *
 * @param <T> The type response.
 * @author gaston.ortiz@mulesoft.com
 */
public class RequestBuilder<T> {

    private static final Logger logger = LoggerFactory.getLogger(RequestBuilder.class);
    private final Client client;
    private final RequestMethodStrategy method;
    private final String path;
    private final Map<String, List<String>> headers = new HashMap<>();
    private final Map<String, List<String>> queryParams = new HashMap<>();
    private final Map<String, Object> pathParams = new HashMap<>();
    private Object entity;
    private String contentType = APPLICATION_XML;
    private String accept = APPLICATION_XML;
    private Class<T> responseType;
    private ResponseHandler<T> responseHandler = new DefaultXMLResponseHandler<>();

    private RequestBuilder(Client client, RequestMethodStrategy method, String path) {
        this.client = client;
        this.method = method;
        this.path = path;
    }

    public RequestBuilder<T> responseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    public RequestBuilder<T> responseHandler(ResponseHandler<T> responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    public RequestBuilder<T> header(String key, Object value) {
        if (Optional.fromNullable(value).isPresent() && StringUtils.isNotEmpty(value.toString())) {
            headers.put(key, Lists.newArrayList(value.toString()));
        }
        return this;
    }

    public RequestBuilder<T> queryParam(String key, Object value) {
        if (Optional.fromNullable(value).isPresent() && StringUtils.isNotEmpty(value.toString())) {
            queryParams.put(key, Lists.newArrayList(value.toString()));
        }
        return this;
    }

    public RequestBuilder<T> queryParams(MultivaluedMap<String, String> queryParams) {
        for (Entry<String, List<String>> entry : queryParams.entrySet()) {
            queryParam(entry.getKey(), entry.getValue() == null || entry.getValue().isEmpty()? null : entry.getValue().get(0));
        }
        return this;
    }

    public RequestBuilder<T> pathParam(String key, Object value) {
        this.pathParams.put(key, value);
        return this;
    }

    public RequestBuilder<T> entity(Object entity) {
        this.entity = entity;
        return this;
    }

    public RequestBuilder<T> accept(String accept) {
        this.accept = accept;
        return this;
    }

    public RequestBuilder<T> contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public T execute() {

        // Setting the path.
        WebTarget target = client.target("").path(path);

        // Resolving path params.
        target = target.resolveTemplates(pathParams);

        // Adding query params.
        for (Entry<String, List<String>> entry : queryParams.entrySet()) {
            target = target.queryParam(entry.getKey(), entry.getValue().toArray()[0]);
        }
        logger.debug("Target path is: {} {}", method, target.getUri());

        // Adding headers.
        Builder requestBuilder = target.request().accept(accept);
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue().toArray()[0]);
            logger.debug("Header: '{}': {}", entry.getKey(), entry.getValue().toArray()[0]);
        }

        // Executing the request.
        final Response response = method.execute(requestBuilder, Optional.fromNullable(entity).or(new Form()), contentType);
        logger.debug("Executed Request with Entity: {}", entity);

        // Buffer the stream so that we may examine it again later in the case of an error.
        response.bufferEntity();
        logger.debug("Response buffered.");

        logger.debug("Parsing response.");
        return responseHandler.handleResponse(response, responseType);
    }

    public static <T> RequestBuilder<T> get(Client client, String path) {
        return new RequestBuilder<T>(client, new GetStrategy(), path);
    }

    public static <T> RequestBuilder<T> post(Client client, String path) {
        return new RequestBuilder<T>(client, new PostStrategy(), path);
    }

    public static <T> RequestBuilder<T> put(Client client, String path) {
        return new RequestBuilder<T>(client, new PutStrategy(), path);
    }

    public static <T> RequestBuilder<T> delete(Client client, String path) {
        return new RequestBuilder<T>(client, new DeleteStrategy(), path);
    }
}
