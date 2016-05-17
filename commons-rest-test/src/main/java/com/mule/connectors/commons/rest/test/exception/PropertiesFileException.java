package com.mule.connectors.commons.rest.test.exception;

/**
 * Exception thrown when an error loading the properties files occurs.
 */
public class PropertiesFileException extends TestCaseException {

    public PropertiesFileException(Exception cause) {
        super("An error occurred while trying to fetch the properties file for the test cases.", cause);
    }
}
