package org.mule.modules.wsdl2connector.generator;

import org.apache.maven.plugin.Mojo;

import static java.lang.String.format;

public interface Loggeable {

    Mojo getMojo();

    default void info(String message, Object... params) {
        if (getMojo() == null) {
            System.out.println(format(message, params));
        } else {
            getMojo().getLog().info(format(message, params));
        }
    }

    default void debug(String message, Object... params) {
        if (getMojo() == null) {
            System.out.println(format(message, params));
        } else {
            getMojo().getLog().debug(format(message, params));
        }
    }
}
