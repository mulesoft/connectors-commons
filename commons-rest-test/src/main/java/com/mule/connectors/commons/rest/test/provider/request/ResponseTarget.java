package com.mule.connectors.commons.rest.test.provider.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Pattern;

public class ResponseTarget {
    private final String key;
    private final Pattern regex;

    @JsonCreator
    public ResponseTarget(@JsonProperty("key") String key, @JsonProperty("regex") String regex) {
        this.key = key;
        this.regex = Pattern.compile(regex);
    }

    public String getKey() {
        return key;
    }

    public String match(String response) {
        return regex.matcher(response).group();
    }
}
