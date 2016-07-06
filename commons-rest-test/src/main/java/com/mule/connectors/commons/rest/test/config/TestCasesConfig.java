package com.mule.connectors.commons.rest.test.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.nio.file.Paths;

import static java.nio.file.Files.exists;

/**
 * Default configuration Class for the API. It tries to load from a set config file and if it doesn't exist, then it will load the default configuration file.
 */
public class TestCasesConfig {

    private final File testCasesDirectory;

    public TestCasesConfig(String configFilePath) throws ConfigurationException {
        PropertiesConfiguration configuration = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class).configure(
                new Parameters().properties().setFileName(exists(Paths.get(configFilePath).toAbsolutePath()) ? configFilePath : "default-cases-config.properties"))
                .getConfiguration();
        this.testCasesDirectory = new File(configuration.getString("resources.location", ""));
    }

    public File getTestCasesDirectory() {
        return testCasesDirectory;
    }
}
