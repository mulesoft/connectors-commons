package org.mule.modules.wsdl2connector.generator.io.writer;

import org.apache.maven.plugin.Mojo;
import org.apache.velocity.VelocityContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

public class ConnectorClassWriter extends SingleClassWriter {

    private final List<Class<?>> clients;

    public ConnectorClassWriter(Mojo mojo, String basePath, String basePackage, Class<?>...clients) {
        super("BaseConnector.vm", "BaseConnector", mojo, basePath, basePackage);
        this.clients = asList(clients);
    }

    @Override
    protected void write(VelocityContext context) {
        Set<String> processors = clients.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .map(Method::getName)
                .collect(toSet());
        info("Adding %s processors to connector.", processors.size());
        context.put("processors", processors);
    }
}
