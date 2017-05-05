package org.mule.modules.wsdl2connector;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.mule.modules.wsdl2connector.generator.ConnectorGenerator;
import org.mule.modules.wsdl2connector.generator.model.CxfTarget;

/**
 */
@Mojo(name = "connector-generate")
public class ConnectorGenerateMojo extends AbstractMojo {

    @Parameter(property = "basePath")
    private String basePath;

    @Parameter(property = "basePackage")
    private String basePackage;

    @Parameter(property = "kerberosGeneratedFilesPath")
    private String kerberosGeneratedFilesPath;

    @Parameter(property = "kerberosServiceClass")
    private String kerberosServiceClass;

    @Parameter(property = "kerberosClientRetrievalMethod")
    private String kerberosClientRetrievalMethod;

    public void execute() throws MojoExecutionException {
        new ConnectorGenerator(this).generate(basePackage, basePath, new CxfTarget(kerberosGeneratedFilesPath, kerberosServiceClass, kerberosClientRetrievalMethod));
    }
}
