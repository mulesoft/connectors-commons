package com.mule.connectors.commons.rest.builder.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

public class SimpleRequest implements Request {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRequest.class);
    private final Method method;
    private String path;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> pathParams = new HashMap<>();
    private Object entity = new Form();
    private String contentType = APPLICATION_XML;
    private String accept = APPLICATION_XML;

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JaxbAnnotationModule());

    public SimpleRequest(Method method) {
        this.method = method;
    }

    @Override
    public Response execute(Client client) {
        // Setting the path.
        WebTarget target = client.target("").path(getPath());

        // Resolving path params.
        target = target.resolveTemplates(Maps.<String, Object>newHashMap(getPathParams()));

        // Adding query params.
        for (Map.Entry<String, String> entry : getQueryParams().entrySet()) {
            target = target.queryParam(entry.getKey(), entry.getValue());
        }
        logger.debug("Request method is {}", getMethod());
        logger.debug("Target path is: {}", target.getUri());

        // Adding headers.
        Invocation.Builder requestBuilder = target.request().accept(getAccept());
        for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
            logger.debug("Header: '{}': {}", entry.getKey(), entry.getValue());
        }

        if(getContentType().equals(APPLICATION_FORM_URLENCODED) && (Optional.fromNullable(getEntity())).isPresent()){
            setEntity(new Form(new MultivaluedHashMap(objectMapper.convertValue(getEntity(), Map.class))));
        }

        // Executing the request.
        Response response = getMethod().execute(requestBuilder, getEntity(), getContentType());
        logger.debug("Executed Request with Entity: {}", getEntity());

        // Buffer the stream so that we may examine it again later in the case of an error.
        response.bufferEntity();
        logger.debug("Response buffered.");
        return response;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public Map<String, String> getPathParams() {
        return pathParams;
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getAccept() {
        return accept;
    }

    @Override
    public Method getMethod() {
        return method;
    }

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

    public void addPathParam(String key, String value) {
        this.pathParams.put(key, value);
    }

    public void setPathParams(Map<String, String> pathParams) {
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
