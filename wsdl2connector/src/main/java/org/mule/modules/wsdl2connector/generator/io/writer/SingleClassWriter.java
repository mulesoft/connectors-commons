package org.mule.modules.wsdl2connector.generator.io.writer;

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

import static java.lang.String.format;

public class SingleClassWriter implements ClassWriter {

    private final String basePath;
    private final VelocityEngine velocityEngine;
    private final Mojo mojo;
    private final String template;
    private final String basePackage;
    private final String name;

    public SingleClassWriter(String name, Mojo mojo, String basePath, String basePackage) {
        this(format("%s.vm", name), name, mojo, basePath, basePackage);
    }

    public SingleClassWriter(String template, String name, Mojo mojo, String basePath, String basePackage) {
        this.template = template;
        this.name = name;
        this.mojo = mojo;
        this.basePath = basePath;
        this.basePackage = basePackage;
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
    }

    public void write() {
        try (PrintWriter writer = getWriter()) {
            VelocityContext context = new VelocityContext();
            context.put("basePackage", basePackage);
            write(context);
            velocityEngine.getTemplate(template, "UTF-8").merge(context, writer);
        } catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException e) {
            // @FIXME: This should not throw RuntimeException.
            throw new RuntimeException(e);
        }
    }

    protected void write(VelocityContext context) {
        // Let child classes implement these methods.
    }

    private PrintWriter getWriter() {
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

    public String getName() {
        return name;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getTemplate() {
        return template;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    @Override
    public Mojo getMojo() {
        return mojo;
    }
}
