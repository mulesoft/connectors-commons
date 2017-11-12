package org.mule.connectors.commons.template.operation;

import org.mule.connectors.atlantic.commons.builder.clazz.ClassBuilder;
import org.mule.connectors.atlantic.commons.builder.execution.ExecutionBuilder;
import org.mule.connectors.atlantic.commons.builder.lambda.function.BiFunction;
import org.mule.connectors.commons.template.config.ConnectorConfig;
import org.mule.connectors.commons.template.connection.ConnectorConnection;
import org.mule.connectors.commons.template.service.ConnectorService;

/**
 * Parent class for Certified Connector Operations that provides with a factory method that allows the fluent execution
 * of the methods of the service.
 *
 * @param <CONFIG> The config object that handles this operation.
 * @param <CONNECTION> The type of connection to be handled by these operations.
 * @param <SERVICE> The service that handles the operations connection to the remote system.
 */
public class ConnectorOperations<CONFIG extends ConnectorConfig, CONNECTION extends ConnectorConnection, SERVICE extends ConnectorService> {

    private final BiFunction<CONFIG, CONNECTION, SERVICE> serviceConstructor;

    /**
     * Default constructor. This constructor should be called with the
     * @param serviceConstructor
     */
    public ConnectorOperations(BiFunction<CONFIG, CONNECTION, SERVICE> serviceConstructor) {
        this.serviceConstructor = serviceConstructor;
    }

    protected ExecutionBuilder<SERVICE> newExecutionBuilder(CONFIG config, CONNECTION connection) {
        return new ClassBuilder().create(serviceConstructor)
                .withParam(config)
                .withParam(connection);
    }
}
