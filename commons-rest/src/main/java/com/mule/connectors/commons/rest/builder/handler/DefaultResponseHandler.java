package com.mule.connectors.commons.rest.builder.handler;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import java.lang.reflect.Type;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

public class DefaultResponseHandler<T> implements ResponseHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultResponseHandler.class);

    @Override
    public T handleResponse(final Response response, final Type responseType) {
        Status status = Status.fromStatusCode(response.getStatus());
        Family family = response.getStatusInfo().getFamily();
        logger.debug("Response Status is {}", status);
        logger.trace("Response body:\n{}", response.readEntity(String.class));
        if (SUCCESSFUL != family) {
            throw new RequestFailedException(response);
        }

        // Parsing the successful response if necessary.
        return Optional.fromNullable(responseType).transform(new Function<Type, T>() {

            @Override
            public T apply(Type input) {
                logger.debug("Parsing response to an instance of {}", input);
                return response.readEntity(new GenericType<T>(input));
            }
        }).orNull();
    }
}
