package org.mule.modules.wsdl2connector.generator.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ModeledClass extends ModeledMember {
    private final Set<String> imports;
    private final String basePackage;

    public ModeledClass(String basePackage, String name, String...imports) {
        this(basePackage, name, Arrays.asList(imports));

    }

    public ModeledClass(String basePackage, String name, Collection<String> imports) {
        super(name);
        this.basePackage = basePackage;
        Set<String> importSet = new HashSet<>();
        importSet.addAll(imports);
        this.imports = importSet;
    }

    public Set<String> getImports() {
        return imports;
    }

    public String getBasePackage() {
        return basePackage;
    }

}
