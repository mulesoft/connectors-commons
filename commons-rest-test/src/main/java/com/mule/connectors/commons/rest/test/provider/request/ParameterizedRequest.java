package com.mule.connectors.commons.rest.test.provider.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mule.connectors.commons.rest.builder.request.Method;
import com.mule.connectors.commons.rest.builder.request.SimpleRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterizedRequest extends SimpleRequest {
    private Map<String, String> placeholderStore = new HashMap<>();
    private List<ResponseTarget> responseTargets = new ArrayList<>();

    @JsonCreator
    public ParameterizedRequest(@JsonProperty("method") Method behavior) {
        super(behavior);
    }

    @JsonProperty("responseTargets")
    public List<ResponseTarget> getResponseTargets() {
        return responseTargets;
    }

    public void setResponseTargets(List<ResponseTarget> responseTargets) {
        this.responseTargets = responseTargets;
    }

    @JsonProperty("path")
    public String getPath() {
        return resolvePlaceholders(super.getPath());
    }

    @JsonProperty("headers")
    public Map<String, String> getHeaders() {
        return resolvePlaceholders(super.getHeaders());
    }

    @JsonProperty("queryParams")
    public Map<String, String> getQueryParams() {
        return resolvePlaceholders(super.getQueryParams());
    }

    @JsonProperty("pathParams")
    public Map<String, String> getPathParams() {
        return resolvePlaceholders(super.getPathParams());
    }

    @JsonProperty("body")
    public Object getEntity() {
        return super.getEntity();
    }

    @JsonProperty("contentType")
    public String getContentType() {
        return super.getContentType();
    }

    @JsonProperty("accept")
    public String getAccept() {
        return super.getAccept();
    }

    public void addPlaceHolders(Map<String, String> placeholderStore) {
        this.placeholderStore.putAll(placeholderStore);
    }

    private <T> Map<String, String> resolvePlaceholders(Map<String, T> input) {
        Map<String, String> output = new HashMap<>();
        for (Map.Entry<String, T> entry : input.entrySet()) {
            output.put(entry.getKey(), resolvePlaceholders(entry.getValue().toString()));
        }
        return output;

    }

    private String resolvePlaceholders(String input) {
        String output = input;
        for (Map.Entry<String, String> entry : placeholderStore.entrySet()) {
            output = input.replace(String.format("${%s}", entry.getKey()), entry.getValue());
        }
        return output;
    }
}
