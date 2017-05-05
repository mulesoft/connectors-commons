package org.mule.modules.wsdl2connector.generator;

import org.mule.modules.wsdl2connector.generator.exception.WSDL2ConnectorException;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * Exception thrown when the generated classes
 */
public class GeneratedClassesCompilationException extends WSDL2ConnectorException {

    private final DiagnosticCollector<JavaFileObject> diagnostics;

    public GeneratedClassesCompilationException(DiagnosticCollector<JavaFileObject> diagnostics) {
        super(diagnostics.getDiagnostics().stream()
                .map(diagnostic -> format("Error on line %d in %s", diagnostic.getLineNumber(), diagnostic.getSource().toUri()))
                .collect(joining("\n")));
        this.diagnostics = diagnostics;
    }

    public DiagnosticCollector<JavaFileObject> getDiagnostics() {
        return diagnostics;
    }
}
