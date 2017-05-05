package org.mule.modules.wsdl2connector.generator.model;

public class CxfTarget {

    private final String serviceFullyQualifiedName;
    private final String clientRetrievalMethod;
    private final String basePath;

    public CxfTarget(String basePath, String serviceFullyQualifiedName, String clientRetrievalMethod) {
        this.basePath = basePath;
        this.serviceFullyQualifiedName = serviceFullyQualifiedName;
        this.clientRetrievalMethod = clientRetrievalMethod;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getClientRetrievalMethod() {
        return clientRetrievalMethod;
    }

    public String getServiceFullyQualifiedName() {
        return serviceFullyQualifiedName;
    }
}
