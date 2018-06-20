package com.mulesoft.extensions.request.builder.handler;

import com.mulesoft.extensions.request.builder.exception.ResponseEntityParsingException;
import org.apache.commons.io.IOUtils;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;

public class DefaultResponseHandler extends StandardResponseHandler<String> {

    @Override
    protected String handleSuccessfulResponse(HttpResponse response) {
        try {
            return IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new ResponseEntityParsingException(e);
        }
    }
}
