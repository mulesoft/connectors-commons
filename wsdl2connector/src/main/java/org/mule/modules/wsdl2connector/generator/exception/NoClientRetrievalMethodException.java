package org.mule.modules.wsdl2connector.generator.exception;

/**
 * Exception thrown when the method to get the client from the service was not found.
 */
public class NoClientRetrievalMethodException extends WSDL2ConnectorException {

    private final String serviceFullyQualifiedName;
    private final String clientRetrievalMethod;

    public NoClientRetrievalMethodException(String serviceFullyQualifiedName, String clientRetrievalMethod, Throwable cause) {
        super("No client retrieval method called %s was found for service %s", cause, clientRetrievalMethod, serviceFullyQualifiedName);
        this.serviceFullyQualifiedName = serviceFullyQualifiedName;
        this.clientRetrievalMethod = clientRetrievalMethod;
    }

    public String getServiceFullyQualifiedName() {
        return serviceFullyQualifiedName;
    }

    public String getClientRetrievalMethod() {
        return clientRetrievalMethod;
    }
}
