package com.mulesoft.extensions.request.builder.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulesoft.extensions.request.builder.exception.ResponseEntityParsingException;
import org.apache.commons.io.IOUtils;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Optional;

import static java.util.function.Predicate.isEqual;

public class JacksonResponseHandler<T> extends StandardResponseHandler<T> {
    private final ObjectMapper objectMapper;
    private final JavaType javaType;

    public JacksonResponseHandler(Type type) {
        this(type, new ObjectMapper());
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    public JacksonResponseHandler(Type type, ObjectMapper objectMapper) {
        this.javaType = objectMapper.constructType(type);
        this.objectMapper = objectMapper;
    }

    @Override
    protected T handleSuccessfulResponse(HttpResponse response) {
        return (T) Optional.ofNullable(response.getEntity().getContent())
                .map(content -> {
                    try {
                        return IOUtils.toString(content, Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        throw new ResponseEntityParsingException(e);
                    }
                })
                .filter(isEqual("").negate())
                .map(string -> {
                    try {
                        return objectMapper.readValue(string, javaType);
                    } catch (IOException e) {
                        throw new ResponseEntityParsingException(e);
                    }
                }).orElse(null);
    }
}
