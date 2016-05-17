package com.mule.connectors.commons.rest.test.config;

import com.google.common.base.Optional;
import com.mule.connectors.commons.rest.test.exception.PropertiesFileException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class TestCasesConfig {

    private final File testCasesDirectory;

    public TestCasesConfig(String configFilePath) {
        try (InputStream inputStream = openStream(configFilePath); InputStream defaultStream = openStream("/default-cases-config.properties")) {
            Properties properties = new Properties();
            properties.load(Optional.fromNullable(inputStream).or(defaultStream));
            this.testCasesDirectory = new File(properties.getProperty("resources.location"));
        } catch (IllegalArgumentException | IOException e) {
            throw new PropertiesFileException(e);
        }
    }

    protected InputStream openStream(String propertiesFile) throws IOException {
        return getClass().getResourceAsStream(propertiesFile);
    }

    public File getTestCasesDirectory() {
        return testCasesDirectory;
    }
}
