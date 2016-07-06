package com.mule.connectors.commons.rest.test.exception;

/**
 * Parent Exception from which all exceptions on this module should extend.
 */
public class TestCaseException extends RuntimeException {

    public TestCaseException(String message) {
        super(message);
    }

    public TestCaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
