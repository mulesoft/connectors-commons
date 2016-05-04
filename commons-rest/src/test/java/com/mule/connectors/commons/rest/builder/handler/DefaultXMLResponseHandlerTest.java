package com.mule.connectors.commons.rest.builder.handler;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.security.Permission;
import java.util.Date;

import static org.easymock.EasyMock.*;

public class DefaultXMLResponseHandlerTest {

    private Response response;
    private int responseStatusCode;
    private Object expectedParsedResponse;
    private Response.StatusType statusType;

    @Before
    public void before() {
        response = createMock(Response.class);
        responseStatusCode = Response.Status.OK.getStatusCode();
        statusType = EasyMock.createMock(Response.StatusType.class);
        expect(response.getStatusInfo()).andReturn(statusType).anyTimes();
    }

    @Test
    public void testSimpleHandleResponse() throws IllegalAccessException, InstantiationException {
        expect(response.getStatus()).andReturn(responseStatusCode);
        expect(response.readEntity(eq(String.class))).andReturn("");
        expect(statusType.getFamily()).andReturn(Response.Status.Family.SUCCESSFUL);

        expectedParsedResponse = new Date();
        expect(response.readEntity(eq(expectedParsedResponse.getClass()))).andReturn(expectedParsedResponse);
        replay(response, statusType);

        Assert.assertEquals(expectedParsedResponse, new DefaultXMLResponseHandler().handleResponse(response, expectedParsedResponse.getClass()));
    }

    @Test
    public void testVoidHandleResponse() throws IllegalAccessException, InstantiationException {
        expect(response.getStatus()).andReturn(responseStatusCode);
        expect(response.readEntity(eq(String.class))).andReturn("");
        expect(statusType.getFamily()).andReturn(Response.Status.Family.SUCCESSFUL);
        replay(response, statusType);

        Assert.assertNull(new DefaultXMLResponseHandler().handleResponse(response, null));
    }

    @Test
    public void testEmptyResponseHandleResponse() throws IllegalAccessException, InstantiationException {
        expect(response.getStatus()).andReturn(Response.Status.NO_CONTENT.getStatusCode());
        expect(response.readEntity(eq(String.class))).andReturn("");
        expect(statusType.getFamily()).andReturn(Response.Status.Family.SUCCESSFUL);
        replay(response, statusType);

        Assert.assertNull(new DefaultXMLResponseHandler().handleResponse(response, null));
    }

    @Test(expected = ForbiddenException.class)
    public void testForbiddenExceptionHandleResponse() {
        responseStatusCode = Response.Status.FORBIDDEN.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = BadRequestException.class)
    public void testBadRequestExceptionHandleResponse() {
        responseStatusCode = Response.Status.BAD_REQUEST.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = NotAuthorizedException.class)
    public void testNotAuthorizedExceptionHandleResponse() {
        responseStatusCode = Response.Status.UNAUTHORIZED.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundExceptionHandleResponse() {
        responseStatusCode = Response.Status.NOT_FOUND.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = NotAllowedException.class)
    public void testNotAllowedExceptionHandleResponse() {
        responseStatusCode = Response.Status.METHOD_NOT_ALLOWED.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = NotAcceptableException.class)
    public void testNotAcceptableExceptionHandleResponse() {
        responseStatusCode = Response.Status.NOT_ACCEPTABLE.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = NotSupportedException.class)
    public void testNotSupportedExceptionHandleResponse() {
        responseStatusCode = Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = InternalServerErrorException.class)
    public void testInternalServerErrorExceptionHandleResponse() {
        responseStatusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testServiceUnavailableExceptionHandleResponse() {
        responseStatusCode = Response.Status.SERVICE_UNAVAILABLE.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = RedirectionException.class)
    public void testRedirectionExceptionHandleResponse() {
        responseStatusCode = Response.Status.TEMPORARY_REDIRECT.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = ClientErrorException.class)
    public void testUnmappedClientErrorExceptionHandleResponse() {
        responseStatusCode = Response.Status.EXPECTATION_FAILED.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = ServerErrorException.class)
    public void testUnmappedServerErrorExceptionHandleResponse() {
        responseStatusCode = Response.Status.BAD_GATEWAY.getStatusCode();
        testExceptionHandleResponse();
    }

    @Test(expected = WebApplicationException.class)
    public void testUnmappedExceptionHandleResponse() {
        responseStatusCode = 105;
        testExceptionHandleResponse();
    }

    public void testExceptionHandleResponse() {
        Response.Status mappedStatus = Response.Status.fromStatusCode(responseStatusCode);
        Response.Status.Family family = mappedStatus == null ? Response.Status.Family.INFORMATIONAL : mappedStatus.getFamily();
        expect(response.getStatus()).andReturn(responseStatusCode).anyTimes();
        expect(statusType.getFamily()).andReturn(family).anyTimes();
        expect(statusType.getStatusCode()).andReturn(responseStatusCode).anyTimes();
        expect(statusType.getReasonPhrase()).andReturn("").anyTimes();
        expect(response.readEntity(eq(String.class))).andReturn("");
        replay(response, statusType);

        new DefaultXMLResponseHandler().handleResponse(response, null);
    }
}
