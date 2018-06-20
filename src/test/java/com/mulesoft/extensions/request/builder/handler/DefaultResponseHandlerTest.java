package com.mulesoft.extensions.request.builder.handler;

import org.junit.Before;
import org.junit.Test;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import static org.easymock.EasyMock.createMock;

public class DefaultResponseHandlerTest {

    private HttpResponse response;
    private Object expectedParsedResponse;

    @Before
    public void before() {
        response = createMock(HttpResponse.class);
    }

    @Test
    public void testSimpleHandleResponse() throws IllegalAccessException, InstantiationException {
/*        expect(response.getStatusCode()).andReturn(200);
        expect(response.getEntity()).andReturn(new EmptyHttpEntity());

        expectedParsedResponse = new Date();
        expect(response.readEntity(anyObject(GenericType.class))).andReturn(expectedParsedResponse);
        replay(response, statusType);

        assertThat(new DefaultResponseHandler().handleResponse(response, expectedParsedResponse.getClass()), instanceOf(expectedParsedResponse.getClass()));
        verify(response, statusType);
    }

    @Test
    public void testTypedHandleResponse() throws IllegalAccessException, InstantiationException {
        expect(response.getStatus()).andReturn(OK.getStatusCode());
        expect(response.readEntity(eq(String.class))).andReturn("");
        expect(statusType.getFamily()).andReturn(SUCCESSFUL);

        expectedParsedResponse = new HashMap<String, Object>();
        expect(response.readEntity(anyObject(GenericType.class))).andReturn(expectedParsedResponse);
        replay(response, statusType);

        assertThat(new DefaultResponseHandler().handleResponse(response, new ParameterizedTypeImpl(Map.class, String.class, Object.class)), instanceOf(Map.class));
        verify(response, statusType);
    }

    @Test
    public void testVoidHandleResponse() throws IllegalAccessException, InstantiationException {
        expect(response.getStatus()).andReturn(OK.getStatusCode());
        expect(response.readEntity(eq(String.class))).andReturn("");
        expect(statusType.getFamily()).andReturn(SUCCESSFUL);
        replay(response, statusType);

        Assert.assertNull(new DefaultResponseHandler().handleResponse(response, null));
        verify(response, statusType);
    }

    @Test
    public void testEmptyResponseHandleResponse() throws IllegalAccessException, InstantiationException {
        expect(response.getStatus()).andReturn(Response.Status.NO_CONTENT.getStatusCode());
        expect(response.readEntity(eq(String.class))).andReturn("");
        expect(statusType.getFamily()).andReturn(SUCCESSFUL);
        replay(response, statusType);

        Assert.assertNull(new DefaultResponseHandler().handleResponse(response, null));
        verify(response, statusType);
    }

    @Test
    public void testForbiddenExceptionHandleResponse() {
        testExceptionHandleResponse(FORBIDDEN.getStatusCode());
    }

    @Test
    public void testBadRequestExceptionHandleResponse() {
        testExceptionHandleResponse(BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testNotAuthorizedExceptionHandleResponse() {
        testExceptionHandleResponse(UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void testNotFoundExceptionHandleResponse() {
        testExceptionHandleResponse(NOT_FOUND.getStatusCode());
    }

    @Test
    public void testNotAllowedExceptionHandleResponse() {
        testExceptionHandleResponse(METHOD_NOT_ALLOWED.getStatusCode());
    }

    @Test
    public void testNotAcceptableExceptionHandleResponse() {
        testExceptionHandleResponse(NOT_ACCEPTABLE.getStatusCode());
    }

    @Test
    public void testNotSupportedExceptionHandleResponse() {
        testExceptionHandleResponse(UNSUPPORTED_MEDIA_TYPE.getStatusCode());
    }

    @Test
    public void testInternalServerErrorExceptionHandleResponse() {
        testExceptionHandleResponse(INTERNAL_SERVER_ERROR.getStatusCode());
    }

    @Test
    public void testServiceUnavailableExceptionHandleResponse() {
        testExceptionHandleResponse(SERVICE_UNAVAILABLE.getStatusCode());
    }

    @Test
    public void testRedirectionExceptionHandleResponse() {
        testExceptionHandleResponse(TEMPORARY_REDIRECT.getStatusCode());
    }

    @Test
    public void testUnmappedClientErrorExceptionHandleResponse() {
        testExceptionHandleResponse(EXPECTATION_FAILED.getStatusCode());
    }

    @Test
    public void testUnmappedServerErrorExceptionHandleResponse() {
        testExceptionHandleResponse(BAD_GATEWAY.getStatusCode());
    }

    @Test
    public void testUnmappedExceptionHandleResponse() {
        testExceptionHandleResponse(105);
    }

    public void testExceptionHandleResponse(int responseStatusCode) {
        try {
            Response.Status mappedStatus = Response.Status.fromStatusCode(responseStatusCode);
            Response.Status.Family family = mappedStatus == null ? Response.Status.Family.INFORMATIONAL : mappedStatus.getFamily();
            expect(response.getStatus()).andReturn(responseStatusCode).anyTimes();
            expect(statusType.getFamily()).andReturn(family).anyTimes();
            expect(statusType.getStatusCode()).andReturn(responseStatusCode).anyTimes();
            expect(statusType.getReasonPhrase()).andReturn("").anyTimes();
            expect(response.readEntity(eq(String.class))).andReturn("");
            replay(response, statusType);
            new DefaultResponseHandler().handleResponse(response, null);
            fail("Expected an exception here.");
        } catch (RequestFailedException e) {
            assertThat(e.getResponse(), is(response));
            assertThat(e.getStatusCode(), is(responseStatusCode));
        }*/
    }
}
