package com.mule.connectors.commons.rest.builder;

import com.google.common.base.Optional;
import com.mule.connectors.commons.rest.builder.handler.DefaultResponseHandler;
import com.mule.connectors.commons.rest.builder.handler.ResponseHandler;
import com.mule.connectors.commons.rest.builder.request.SimpleRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

import static com.mule.connectors.commons.rest.builder.request.Method.*;

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
    private final SimpleRequest request;
    private Type responseType;
    private ResponseHandler<T> responseHandler = new DefaultResponseHandler<>();

    private RequestBuilder(Client client, SimpleRequest request, String path) {
        this.client = client;
        this.request = request;
        this.request.setPath(path);
    }

    public RequestBuilder<T> responseType(Type responseType) {
        this.responseType = responseType;
        return this;
    }

    public RequestBuilder<T> responseHandler(ResponseHandler<T> responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    public RequestBuilder<T> header(String key, Object value) {
        if (Optional.fromNullable(value).isPresent() && StringUtils.isNotEmpty(value.toString())) {
            this.request.addHeader(key, value.toString());
        }
        return this;
    }

    public RequestBuilder<T> basicAuthorization(String username, String password) {
        return header("Authorization",
                String.format("Basic %s", DatatypeConverter.printBase64Binary(String.format("%s:%s", username, password).getBytes(Charset.forName("UTF-8")))));
    }

    public RequestBuilder<T> queryParam(String key, Object value) {
        if (Optional.fromNullable(value).isPresent() && StringUtils.isNotEmpty(value.toString())) {
            this.request.addQueryParam(key, value.toString());
        }
        return this;
    }

    public RequestBuilder<T> queryParams(MultivaluedMap<String, String> queryParams) {
        for (Entry<String, List<String>> entry : queryParams.entrySet()) {
            queryParam(entry.getKey(), entry.getValue() == null || entry.getValue().isEmpty() ? null : entry.getValue().get(0));
        }
        return this;
    }

    public RequestBuilder<T> pathParam(String key, Object value) {
        if (Optional.fromNullable(value).isPresent() && StringUtils.isNotEmpty(value.toString())) {
            this.request.addPathParam(key, value.toString());
        }
        return this;
    }

    public RequestBuilder<T> entity(Object entity) {
        this.request.setEntity(entity);
        return this;
    }

    public RequestBuilder<T> accept(String accept) {
        this.request.setAccept(accept);
        return this;
    }

    public RequestBuilder<T> contentType(String contentType) {
        this.request.setContentType(contentType);
        return this;
    }

    public T execute() {
        Response response = request.execute(client);
        logger.debug("Parsing response.");
        return responseHandler.handleResponse(response, responseType);
    }

    public static <T> RequestBuilder<T> get(Client client, String path) {
        return new RequestBuilder<>(client, new SimpleRequest(GET), path);
    }

    public static <T> RequestBuilder<T> post(Client client, String path) {
        return new RequestBuilder<>(client, new SimpleRequest(POST), path);
    }

    public static <T> RequestBuilder<T> put(Client client, String path) {
        return new RequestBuilder<>(client, new SimpleRequest(PUT), path);
    }

    public static <T> RequestBuilder<T> delete(Client client, String path) {
        return new RequestBuilder<>(client, new SimpleRequest(DELETE), path);
    }
}
