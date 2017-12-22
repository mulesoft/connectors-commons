package com.mule.connectors.plugins.model;

import java.util.List;

public class Config {
    private List<Connector> connectors;

    public List<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }
}
