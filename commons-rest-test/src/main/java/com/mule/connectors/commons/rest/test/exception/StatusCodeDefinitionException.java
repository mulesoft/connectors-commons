package com.mule.connectors.commons.rest.test.exception;

/**
 * Exception thrown when there is no valid test cases directory on the configured location.
 */
public class StatusCodeDefinitionException extends TestCaseException {

    private static final long serialVersionUID = 1L;

    public StatusCodeDefinitionException(String expectedValue) {
        super(String.format("The provided status code or family ('%s') must be an integer or a number followed by 'xx'. For example: '200', '4xx'.", expectedValue));
    }
}
