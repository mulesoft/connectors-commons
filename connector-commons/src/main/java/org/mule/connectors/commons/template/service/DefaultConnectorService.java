package org.mule.connectors.commons.template.service;

import org.mule.connectors.commons.template.config.ConnectorConfig;
import org.mule.connectors.commons.template.connection.ConnectorConnection;

public class DefaultConnectorService<CONFIG extends ConnectorConfig, CONNECTION extends ConnectorConnection> implements ConnectorService {
    private final CONFIG config;
    private final CONNECTION connection;

    public DefaultConnectorService(CONFIG config, CONNECTION connection) {
        this.config = config;
        this.connection = connection;
    }

    protected CONFIG getConfig() {
        return this.config;
    }

    protected CONNECTION getConnection() {
        return this.connection;
    }
}
