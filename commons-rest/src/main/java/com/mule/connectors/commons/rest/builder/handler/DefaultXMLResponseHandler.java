package com.mule.connectors.commons.rest.builder.handler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class DefaultXMLResponseHandler<T> implements ResponseHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultXMLResponseHandler.class);

    @Override
    public T handleResponse(final Response response, final Type responseType) {
        Status status = Status.fromStatusCode(response.getStatus());
        Family family = response.getStatusInfo().getFamily();
        logger.debug("Response Status is {}", status);
        logger.trace("Response body:\n{}", response.readEntity(String.class));
        if (Family.SUCCESSFUL != family) {
            WebApplicationException exception = null;
            try {
                exception = ResponseStatusExceptionMapper.valueOf(Optional.<Enum<?>>fromNullable(status).or(family).name()).createException(response);
            } catch (IllegalArgumentException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException unmappedStatusException) {
                try {
                    logger.warn("The response status is not mapped.", unmappedStatusException);
                    exception = ResponseStatusExceptionMapper.valueOf(family.name()).createException(response);
                } catch (IllegalArgumentException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException unmappedFamilyException) {
                    exception = new WebApplicationException(unmappedFamilyException, response);
                }
            }
            throw exception;
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
