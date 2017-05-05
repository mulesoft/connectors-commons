package org.mule.modules.wsdl2connector.generator;

import org.apache.maven.plugin.Mojo;
import org.mule.modules.wsdl2connector.generator.io.reader.ClassReader;
import org.mule.modules.wsdl2connector.generator.io.writer.ClassWriter;
import org.mule.modules.wsdl2connector.generator.io.writer.ClassWriterFactory;
import org.mule.modules.wsdl2connector.generator.model.CxfTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Default domain class that handles the main operation of generating sources.
 */
public class ConnectorGenerator {

    private final Mojo mojo;

    public ConnectorGenerator(Mojo mojo) {
        this.mojo = mojo;
    }

    /**
     * Generates sources.
     */
    public void generate(String basePackage, String classpath, CxfTarget kerberosTarget) {

        // Reading CXF classes.
        ClassLoader classLoader = new ClassReader(mojo, classpath).read(kerberosTarget);

        ClassWriterFactory writerFactory = new ClassWriterFactory(mojo, classpath, basePackage);

        // Retrieving clients information.
        ServiceInformation kerberosServiceInformation = new ServiceInformation(classLoader, kerberosTarget);

        List<ClassWriter> writers = new ArrayList<>();

        // Write base config file.
        writers.add(writerFactory.createBaseConfigWriter());

        // Writing utility class for connector.
        writers.add(writerFactory.createEndpointNameFilterWriter());

        // Writing connector class.
        writers.add(writerFactory.createConnectorClassWriter(kerberosServiceInformation.getClientClass()));

        // Write Kerberos config file.
        writers.add(writerFactory.createXRMSpnegoClientAction());
        writers.add(writerFactory.createKerberosConfigClassWriter(kerberosServiceInformation));

        // Write parameter files.
        writers.add(writerFactory.createParameterClassWriter("kerberos", kerberosServiceInformation.getClientClass()));
        // Write NTLM config file.
        // Write Metadata files.

        writers.stream().forEach(ClassWriter::write);
    }
}
