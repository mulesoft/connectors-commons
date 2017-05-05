package org.mule.modules.wsdl2connector.generator;

import org.mule.modules.wsdl2connector.generator.exception.NoClientRetrievalMethodException;
import org.mule.modules.wsdl2connector.generator.exception.NoServiceClassException;
import org.mule.modules.wsdl2connector.generator.model.CxfTarget;

import java.lang.reflect.Method;

public class ServiceInformation {

    private final Class<?> clientClass;
    private final Class<?> serviceClass;
    private final Method clientRetrievalMethod;

    public ServiceInformation(ClassLoader classLoader, CxfTarget target) {
        try {
            this.serviceClass = classLoader.loadClass(target.getServiceFullyQualifiedName());
            this.clientRetrievalMethod = this.serviceClass
                    .getDeclaredMethod(target.getClientRetrievalMethod());
            this.clientClass = this.clientRetrievalMethod.getReturnType();
        } catch (ClassNotFoundException e) {
            throw new NoServiceClassException(target.getServiceFullyQualifiedName(), e);
        } catch (NoSuchMethodException e) {
            throw new NoClientRetrievalMethodException(target.getServiceFullyQualifiedName(), target.getClientRetrievalMethod(), e);
        }
    }

    public Class<?> getClientClass() {
        return clientClass;
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public Method getClientRetrievalMethod() {
        return clientRetrievalMethod;
    }
}
