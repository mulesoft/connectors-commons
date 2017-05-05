package org.mule.modules.wsdl2connector.generator.io.reader;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.maven.plugin.Mojo;
import org.mule.modules.wsdl2connector.generator.GeneratedClassesCompilationException;
import org.mule.modules.wsdl2connector.generator.Loggeable;
import org.mule.modules.wsdl2connector.generator.exception.ClasspathDirectoryNotCreatedException;
import org.mule.modules.wsdl2connector.generator.exception.InvalidClassTargetException;
import org.mule.modules.wsdl2connector.generator.exception.StandardJavaFileManagerClosingException;
import org.mule.modules.wsdl2connector.generator.model.CxfTarget;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static javax.tools.ToolProvider.getSystemJavaCompiler;
import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.filefilter.DirectoryFileFilter.DIRECTORY;

public class ClassReader implements Loggeable {

    private static final RegexFileFilter JAVA_FILTER = new RegexFileFilter(".*\\.java");

    private final Mojo mojo;
    private final String classpath;

    public ClassReader(Mojo mojo, String classpath) {
        this.mojo = mojo;
        this.classpath = classpath;
    }

    public ClassLoader read(CxfTarget... targets) {
        info("Reading java classes.");
        debug("Creating the temporary directory where the classes will be stored on %s.", classpath);
        File classpathDirectory = new File(classpath);
        if (!classpathDirectory.exists()) {
            debug("Classpath directory not found. Creating directory structure.");
            if (!classpathDirectory.mkdirs()) {
                throw new ClasspathDirectoryNotCreatedException(classpathDirectory);
            }
        }

        debug("Scanning for the java classes.");
        List<File> javaFiles = stream(targets)
                .map(CxfTarget::getBasePath)
                .map(File::new)
                .map(target -> listFiles(target, JAVA_FILTER, DIRECTORY))
                .flatMap(Collection::stream)
                .collect(toList());

        debug("Compiling %s found java classes to the temporary directory.", javaFiles.size());
        JavaCompiler compiler = getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
            if (!compiler.getTask(null, fileManager, diagnostics, newArrayList("-classpath", System.getProperty("java.class.path"), "-d", classpath), null,
                    fileManager.getJavaFileObjectsFromFiles(javaFiles)).call()) {
                throw new GeneratedClassesCompilationException(diagnostics);
            }
            debug("Create the class loader instance on temporary directory.");
            return new URLClassLoader(new URL[] { classpathDirectory.toURI().toURL() });
        } catch (MalformedURLException e) {
            throw new InvalidClassTargetException(classpath, e);
        } catch (IOException e) {
            throw new StandardJavaFileManagerClosingException(e);
        }
    }

    @Override
    public Mojo getMojo() {
        return mojo;
    }
}
