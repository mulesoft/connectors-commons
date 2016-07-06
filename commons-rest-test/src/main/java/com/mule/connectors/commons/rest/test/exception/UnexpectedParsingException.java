package com.mule.connectors.commons.rest.test.exception;

import java.io.File;
import java.io.IOException;

/**
 * Exception thrown when an unexpected parsing error occurs while parsing a test case.
 */
public class UnexpectedParsingException extends TestCaseException {

    public UnexpectedParsingException(File testCaseFile, IOException cause) {
        super(String.format("An unknown exception occurred while parsing file '%s'.", testCaseFile.getAbsolutePath()), cause);
    }
}
