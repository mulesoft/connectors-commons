package com.mule.connectors.commons.rest.builder.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * Representation of an HTTP request to be sent.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "method")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GetRequest.class, name = "GET"),
        @JsonSubTypes.Type(value = PostRequest.class, name = "POST"),
        @JsonSubTypes.Type(value = PutRequest.class, name = "PUT"),
        @JsonSubTypes.Type(value = DeleteRequest.class, name = "DELETE")})
public abstract class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);
    @JsonProperty("path")
    private String path;
    @JsonProperty("headers")
    private Map<String, String> headers = new HashMap<>();
    @JsonProperty("queryParams")
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, Object> pathParams = new HashMap<>();
    @JsonProperty("body")
    private Object entity;
    @JsonProperty("contentType")
    private String contentType = APPLICATION_XML;
    @JsonProperty("accept")
    private String accept = APPLICATION_XML;

    public Response execute(Client client) {
        // Setting the path.
        WebTarget target = client.target("").path(path);

        // Resolving path params.
        target = target.resolveTemplates(pathParams);

        // Adding query params.
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            target = target.queryParam(entry.getKey(), entry.getValue());
        }
        logger.debug("Request method is {}", getClass().getSimpleName());
        logger.debug("Target path is: {}", target.getUri());

        // Adding headers.
        Invocation.Builder requestBuilder = target.request().accept(accept);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
            logger.debug("Header: '{}': {}", entry.getKey(), entry.getValue());
        }

        // Executing the request.
        Response response = doExecute(requestBuilder, Optional.fromNullable(entity).or(new Form()), contentType);
        logger.debug("Executed Request with Entity: {}", entity);
        return response;
    }

    protected abstract Response doExecute(Invocation.Builder requestBuilder, Object entity, String contentType);

    public void setPath(String path) {
        this.path = path;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addQueryParam(String key, String value) {
        this.queryParams.put(key, value);
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void addPathParam(String key, Object value) {
        this.pathParams.put(key, value);
    }

    public void setPathParams(Map<String, Object> pathParams) {
        this.pathParams = pathParams;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }
}
