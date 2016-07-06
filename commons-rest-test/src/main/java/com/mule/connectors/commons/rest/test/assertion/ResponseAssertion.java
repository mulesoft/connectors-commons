package com.mule.connectors.commons.rest.test.assertion;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mule.connectors.commons.rest.test.assertion.body.Contains;
import com.mule.connectors.commons.rest.test.assertion.body.Equals;
import com.mule.connectors.commons.rest.test.assertion.header.HeaderContains;
import com.mule.connectors.commons.rest.test.assertion.header.HeaderEquals;
import org.hamcrest.Matcher;

import javax.ws.rs.core.Response;

/**
 * Parent {@link Matcher} for all the Matchers defined on this class. The purpose of this interface is to map the mappers to json.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Contains.class, name = "body"),
        @JsonSubTypes.Type(value = Equals.class, name = "equals"),
        @JsonSubTypes.Type(value = HeaderContains.class, name = "hasHeader"),
        @JsonSubTypes.Type(value = HeaderEquals.class, name = "headerEquals")
})
public interface ResponseAssertion extends Matcher<Response> {

}
