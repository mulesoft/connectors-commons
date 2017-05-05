package org.mule.modules.wsdl2connector.generator.io.writer;

import org.apache.maven.plugin.Mojo;
import org.mule.modules.wsdl2connector.generator.ServiceInformation;

public class ClassWriterFactory {

    private final Mojo mojo;
    private final String basePath;
    private final String basePackage;

    public ClassWriterFactory(Mojo mojo, String basePath, String basePackage) {
        this.mojo = mojo;
        this.basePath = basePath;
        this.basePackage = basePackage;
    }

    public ClassWriter createBaseConfigWriter() {
        return createSingleClassWriter("BaseConfig");
    }

    public ClassWriter createConnectorClassWriter(Class<?>... clients) {
        return new ConnectorClassWriter(mojo, basePath, basePackage, clients);
    }

    public ClassWriter createKerberosConfigClassWriter(ServiceInformation information) {
        return new KerberosConfigClassWriter(mojo, basePath, basePackage, information);
    }

    public ClassWriter createParameterClassWriter(String subpackage, Class<?> client) {
        return new ParameterClassWriter(mojo, basePath, String.format("%s.%s", basePackage, subpackage), client);
    }

    public ClassWriter createEndpointNameFilterWriter() {
        return createSingleClassWriter("EndpointNameFilter");
    }

    public ClassWriter createXRMSpnegoClientAction() {
        return createSingleClassWriter("XRMSpnegoClientAction");
    }

    private ClassWriter createSingleClassWriter(String className) {
        return new SingleClassWriter(className, mojo, basePath, basePackage);
    }
}
