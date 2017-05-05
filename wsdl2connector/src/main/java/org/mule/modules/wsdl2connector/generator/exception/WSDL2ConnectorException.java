package org.mule.modules.wsdl2connector.generator.exception;

import static java.lang.String.format;

public abstract class WSDL2ConnectorException extends RuntimeException {

    public WSDL2ConnectorException(String message, Object... params) {
        super(format(message, params));
    }

    public WSDL2ConnectorException(String message, Throwable cause, Object... params) {
        super(format(message, params), cause);
    }
}
