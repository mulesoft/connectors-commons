package com.mule.connectors.commons.rest.test.assertion.raml;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponse;
import com.mule.connectors.commons.rest.test.assertion.RequestAndResponseAssertion;
import guru.nidi.ramltester.RamlDefinition;
import guru.nidi.ramltester.RamlLoaders;
import guru.nidi.ramltester.core.RamlReport;
import guru.nidi.ramltester.core.RamlViolationMessage;
import guru.nidi.ramltester.core.RamlViolations;
import guru.nidi.ramltester.core.Validation;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.List;

import static guru.nidi.ramltester.core.Validation.*;

public class ValidRaml extends BaseMatcher<RequestAndResponse> implements RequestAndResponseAssertion {

    private final RamlDefinition ramlDefinition;
    private final List<Validation> ramlValidations;

    @JsonCreator
    public ValidRaml(@JsonProperty(value = "file", required = true) String ramlFilePath, @JsonProperty(value = "validations", required = false) List<String> validations) {
        ramlDefinition = RamlLoaders.fromClasspath(getClass()).load(String.format("classpath://%s", ramlFilePath));
        ramlValidations = new ArrayList<>();
        for (Object validation : (validations == null || validations.isEmpty()) ? Lists.newArrayList(URI_PARAMETER, EXAMPLE, EMPTY, PARAMETER) : validations) {
            ramlValidations.add(Validation.valueOf(validation.toString()));
        }
    }

    @Override
    public boolean matches(Object item) {
        return getReport(item).isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("RAML validation");
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        RamlReport report = getReport(item);
        description.appendText("found the following errors:");
        addErrors(description, report.getRequestViolations());
        addErrors(description, report.getResponseViolations());
        addErrors(description, report.getValidationViolations());
    }

    private void addErrors(Description description, RamlViolations errors) {
        for (RamlViolationMessage error : errors) {
            description.appendText("\n\t\t");
            description.appendText(error.getMessage());
        }
    }

    private RamlReport getReport(Object item) {
        RequestAndResponse requestAndResponse = RequestAndResponse.class.cast(item);
        return ramlDefinition.testAgainst(new RamlRequestAdapter(requestAndResponse.getRequest()), new RamlResponseAdapter(requestAndResponse.getResponse()));
    }
}
