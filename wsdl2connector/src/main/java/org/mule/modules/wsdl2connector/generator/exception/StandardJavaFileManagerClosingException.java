package org.mule.modules.wsdl2connector.generator.exception;

import java.io.IOException;

/**
 * Exception thrown when an error occurs while closing a {@link javax.tools.StandardJavaFileManager}
 */
public class StandardJavaFileManagerClosingException extends WSDL2ConnectorException {
    public StandardJavaFileManagerClosingException(IOException cause) {
        super("An error occurred while closing the StandardJavaFileManager.", cause);
    }
}
