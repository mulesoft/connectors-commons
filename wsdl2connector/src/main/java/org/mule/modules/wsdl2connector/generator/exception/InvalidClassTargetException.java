package org.mule.modules.wsdl2connector.generator.exception;

/**
 * Exception thrown when the method to get the client from the service was not found.
 */
public class InvalidClassTargetException extends WSDL2ConnectorException {

    private final String classTarget;

    public InvalidClassTargetException(String classTarget, Throwable cause) {
        super("The provided class target '%s' has an invalid format.", cause, classTarget);
        this.classTarget = classTarget;
    }

    public String getClassTarget() {
        return classTarget;
    }
}
