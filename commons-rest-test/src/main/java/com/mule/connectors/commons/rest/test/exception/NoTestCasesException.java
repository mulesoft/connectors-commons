package com.mule.connectors.commons.rest.test.exception;

/**
 * Exception thrown when there are no test cases available on the test cases directory.
 */
public class NoTestCasesException extends TestCaseException {

    public NoTestCasesException (String location) {
        super(String.format("Directory '%s' doesn't contain any valid test cases (test cases should have the '.case' extension).", location));
    }
}
