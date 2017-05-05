package org.mule.modules.wsdl2connector.generator.io.writer;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.Mojo;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.apache.commons.lang.StringUtils.capitalize;

public class ParameterClassWriter implements ClassWriter {

    private final Mojo mojo;
    private final String basePath;
    private final String basePackage;
    private final VelocityEngine velocityEngine;
    private final Class<?> client;

    public ParameterClassWriter(Mojo mojo, String basePath, String basePackage, Class<?> client) {
        this.mojo = mojo;
        this.basePath = basePath;
        this.basePackage = basePackage;
        this.client = client;
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
    }

    @Override
    public void write() {
        stream(client.getDeclaredMethods()).forEach(this::write);
    }

    private void write(Method method) {
        try (PrintWriter writer = getWriter(format("%sParam", capitalize(method.getName())))) {
            VelocityContext context = new VelocityContext();
            context.put("StringUtils", StringUtils.class);
            context.put("basePackage", basePackage);
            context.put("methodName", method.getName());
            context.put("classes", newArrayList(method.getParameterTypes()));
            velocityEngine.getTemplate("Parameter.vm", "UTF-8").merge(context, writer);
        } catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException e) {
            // @FIXME: This should not throw RuntimeException.
            throw new RuntimeException(e);
        }
    }

    private PrintWriter getWriter(String name) {
        try {
            File packageFile = new File(format("%s/%s", basePath, basePackage.replace(".", "/")));
            if (!packageFile.exists()) {
                debug("Packaging folders for '%s' do not exists, creating folder structure.", packageFile.getAbsolutePath());
                packageFile.mkdirs();
            }
            File writeFile = new File(packageFile, format("%s.java", name));
            if (!writeFile.exists()) {
                debug("File %s doesn't exist. Creating.", writeFile.getAbsolutePath());
                writeFile.createNewFile();
            }
            debug("Writing into file '%s'", writeFile.getAbsolutePath());
            return new PrintWriter(writeFile, "UTF-8");
        } catch (IOException e) {
            // @FIXME: This should not throw RuntimeException.
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mojo getMojo() {
        return mojo;
    }
}
