package org.mule.modules.wsdl2connector.generator.exception;

import java.io.File;

/**
 * Exception thrown when the classpath directory cannot be created.
 */
public class ClasspathDirectoryNotCreatedException extends WSDL2ConnectorException {
    public ClasspathDirectoryNotCreatedException(File classPathDirectory) {
        super("Classpath directory '%s' cannot be created.", classPathDirectory);
    }
}
