package com.mule.connectors.commons.rest.test.assertion.raml;

import com.google.common.collect.Lists;
import com.mule.connectors.commons.rest.builder.request.SimpleRequest;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import java.util.ArrayList;
import java.util.logging.Logger;

import static com.mule.connectors.commons.rest.builder.request.Method.GET;
import static org.easymock.EasyMock.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ValidRamlTest {

    private ValidRaml validRaml;
    private SimpleRequest request;

    @Before
    public void setup() {
        request = new SimpleRequest(GET);
        validRaml = new ValidRaml("api.raml", new ArrayList<String>());
    }

    @Test
    public void matchTest() {
        request.setPath("http://www.google.com/");
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new LoggingFilter(Logger.getLogger(getClass().getSimpleName()), true));
        RequestAndResponse requestAndResponse = new RequestAndResponse(request, request.execute(ClientBuilder.newClient(clientConfig)));
        assertThat(validRaml.matches(requestAndResponse), is(false));
    }

    @Test
    public void notEmptyMatchTest() {
        validRaml = new ValidRaml("api.raml", Lists.newArrayList("EMPTY"));
        request.setPath("http://www.google.com/");
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new LoggingFilter(java.util.logging.Logger.getLogger(getClass().getSimpleName()), true));
        RequestAndResponse requestAndResponse = new RequestAndResponse(request, request.execute(ClientBuilder.newClient(clientConfig)));
        assertThat(validRaml.matches(requestAndResponse), is(false));
    }

    @Test
    public void nullMatchTest() {
        validRaml = new ValidRaml("api.raml", null);
        request.setPath("http://www.google.com/");
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new LoggingFilter(java.util.logging.Logger.getLogger(getClass().getSimpleName()), true));
        RequestAndResponse requestAndResponse = new RequestAndResponse(request, request.execute(ClientBuilder.newClient(clientConfig)));
        assertThat(validRaml.matches(requestAndResponse), is(false));
    }

    @Test
    public void describeToTest() {
        Description description = mock(Description.class);
        expect(description.appendText(eq("RAML validation"))).andReturn(description);
        replay(description);
        validRaml.describeTo(description);
        verify(description);
    }

    @Test
    public void describeMismatchTest() {
        request.setPath("http://www.google.com/a");
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new LoggingFilter(java.util.logging.Logger.getLogger(getClass().getSimpleName()), true));
        RequestAndResponse requestAndResponse = new RequestAndResponse(request, request.execute(ClientBuilder.newClient(clientConfig)));

        Description description = mock(Description.class);
        expect(description.appendText(eq("found the following errors:"))).andReturn(description);
        expect(description.appendText(eq("\n\t\t"))).andReturn(description).times(1);
        expect(description.appendText(eq("Response(301) is not defined on action(GET /a) mime-type('application/xml')"))).andReturn(description);
        replay(description);
        validRaml.describeMismatch(requestAndResponse, description);
        verify(description);
    }
}
