package com.mule.connectors.commons.rest.test.exception;

/**
 * Exception thrown when there is no valid test cases directory on the configured location.
 */
public class TestCasesDirectoryNotFoundException extends TestCaseException {

    public TestCasesDirectoryNotFoundException(String location) {
        super(String.format("There is no test cases directory on path '%s'.", location));
    }
}
