package org.mule.modules.wsdl2connector.generator.exception;

/**
 * Exception thrown when the service class is not found.
 */
public class NoServiceClassException extends WSDL2ConnectorException {

    private final String serviceFullyQualifiedName;

    public NoServiceClassException(String serviceFullyQualifiedName, Throwable cause) {
        super("No service class found with name %s", cause, serviceFullyQualifiedName);
        this.serviceFullyQualifiedName = serviceFullyQualifiedName;
    }

    public String getServiceFullyQualifiedName() {
        return serviceFullyQualifiedName;
    }
}
