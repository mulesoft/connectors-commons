package com.mule.connectors.commons.rest.test.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;

/**
 * Exception thrown when the format of a Test TestSuite file is incorrect.
 */
public class InvalidTestCaseFormatException extends TestCaseException {

    public InvalidTestCaseFormatException(File testCaseFile, JsonProcessingException cause) {
        super(String.format("File '%s' is not properly formatted for a test case.", testCaseFile.getAbsolutePath()), cause);
    }
}
