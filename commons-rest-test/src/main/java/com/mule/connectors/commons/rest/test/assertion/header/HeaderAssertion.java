package com.mule.connectors.commons.rest.test.assertion.header;

import com.mule.connectors.commons.rest.test.assertion.ResponseAssertion;
import org.hamcrest.BaseMatcher;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public abstract class HeaderAssertion extends BaseMatcher<Response> implements ResponseAssertion {

    @Override
    public boolean matches(Object item) {
        return matches(Response.class.cast(item).getHeaders());
    }

    public abstract boolean matches(Map<String, List<Object>> headers);
}
