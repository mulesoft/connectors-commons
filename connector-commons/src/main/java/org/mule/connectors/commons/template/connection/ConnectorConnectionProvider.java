package org.mule.connectors.commons.template.connection;

import org.mule.runtime.api.connection.ConnectionValidationResult;

import static org.mule.runtime.api.connection.ConnectionValidationResult.failure;
import static org.mule.runtime.api.connection.ConnectionValidationResult.success;

public class ConnectorConnectionProvider<C extends ConnectorConnection> {

    public void disconnect(C connection) {
        connection.disconnect();
    }

    public ConnectionValidationResult validate(C connection) {
        try {
            connection.validate();
            return success();
        } catch (Exception e) {
            return failure(e.getMessage(), e);
        }
    }
}
