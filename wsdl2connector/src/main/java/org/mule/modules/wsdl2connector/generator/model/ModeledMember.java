package org.mule.modules.wsdl2connector.generator.model;

public abstract class ModeledMember {
    private String name;

    public ModeledMember(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
