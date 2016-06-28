package com.mule.connectors.commons.rest.test.assertion;

import com.mule.connectors.commons.rest.builder.request.Request;

import javax.ws.rs.core.Response;


public class RequestAndResponse {
    private final Request request;
    private final Response response;

    public RequestAndResponse(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
