package com.mule.connectors.commons.rest.test.assertion.raml;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RamlResponseAdapterTest {

    @Test
    public void getConstructorTest() {
        Response response = mock(Response.class);
        expect(response.getStatus()).andReturn(Response.Status.OK.getStatusCode());
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.putSingle("key", "value");
        expect(response.getHeaders()).andReturn(headers);
        expect(response.getMediaType()).andReturn(MediaType.APPLICATION_JSON_TYPE);
        expect(response.readEntity(eq(String.class))).andReturn("{\"a\":1}");
        replay(response);
        RamlResponseAdapter ramlResponseAdapter = new RamlResponseAdapter(response);
        assertThat(ramlResponseAdapter.getHeaderValues().size(), equalTo(1));
        assertThat(ramlResponseAdapter.getHeaderValues().get("key").size(), equalTo(1));
        assertThat(ramlResponseAdapter.getHeaderValues().get("key").get(0), CoreMatchers.<Object>equalTo("value"));
        assertThat(ramlResponseAdapter.getContentType(), equalTo(MediaType.APPLICATION_JSON));
        assertThat(ramlResponseAdapter.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
        assertThat(ramlResponseAdapter.getContent().length, is(7));
        verify(response);
    }
}
