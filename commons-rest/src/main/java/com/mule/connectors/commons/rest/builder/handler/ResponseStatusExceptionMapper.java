package com.mule.connectors.commons.rest.builder.handler;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;

public enum ResponseStatusExceptionMapper {
    BAD_REQUEST(BadRequestException.class),
    UNAUTHORIZED(NotAuthorizedException.class),
    FORBIDDEN(ForbiddenException.class),
    NOT_FOUND(NotFoundException.class),
    METHOD_NOT_ALLOWED(NotAllowedException.class),
    NOT_ACCEPTABLE(NotAcceptableException.class),
    UNSUPPORTED_MEDIA_TYPE(NotSupportedException.class),
    INTERNAL_SERVER_ERROR(InternalServerErrorException.class),
    SERVICE_UNAVAILABLE(ServiceUnavailableException.class),
    REDIRECTION(RedirectionException.class),
    CLIENT_ERROR(ClientErrorException.class),
    SERVER_ERROR(ServerErrorException.class);

    private final Class<? extends WebApplicationException> exceptionClass;

    ResponseStatusExceptionMapper(Class<? extends WebApplicationException> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public WebApplicationException createException(Response response) {
        try {
            return exceptionClass.getDeclaredConstructor(Response.class).newInstance(response);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new WebApplicationException(e, response);
        }
    }
}
