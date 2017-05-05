package org.mule.modules.wsdl2connector.generator.io.writer;

import org.apache.maven.plugin.Mojo;
import org.apache.velocity.VelocityContext;
import org.mule.modules.wsdl2connector.generator.ServiceInformation;

public class KerberosConfigClassWriter extends SingleClassWriter {

    private final ServiceInformation serviceInformation;

    public KerberosConfigClassWriter(Mojo mojo, String basePath, String basePackage, ServiceInformation serviceInformation) {
        super("KerberosConfig.vm", "KerberosConfig", mojo, basePath, basePackage);
        this.serviceInformation = serviceInformation;
    }

    @Override
    protected void write(VelocityContext context) {
        context.put("clientClass", serviceInformation.getClientClass().getSimpleName());
        context.put("clientClassPackage", serviceInformation.getClientClass().getPackage().getName());
        context.put("serviceClass", serviceInformation.getServiceClass().getSimpleName());
        context.put("serviceClassPackage", serviceInformation.getServiceClass().getPackage().getName());
        context.put("clientRetrievalMethod", serviceInformation.getClientRetrievalMethod().getName());
    }
}
