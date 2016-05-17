package com.mule.connectors.commons.rest.test;

import java.util.ArrayList;
import java.util.List;

public class TestCaseResult {

    private boolean successful = true;
    private List<String> errorMessages = new ArrayList<>();

    public void addError(String message) {
        successful = false;
        errorMessages.add(message);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
