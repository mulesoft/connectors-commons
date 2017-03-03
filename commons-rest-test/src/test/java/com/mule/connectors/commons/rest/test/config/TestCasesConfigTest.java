package com.mule.connectors.commons.rest.test.config;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TestCasesConfigTest {

    private TestCasesConfig config;

    @Test
    public void getTestCasesDirectoryTest() throws ConfigurationException {
        config = new TestCasesConfig("src/test/resources/cases-config.properties");
        assertThat(config.getTestCasesDirectory(), is(new File("src/test/resources/cases/ok")));
    }

    @Test
    public void getDefaultTestCasesDirectoryTest() throws ConfigurationException {
        config = new TestCasesConfig("inexistent.properties");
        assertThat(config.getTestCasesDirectory(), is(new File("cases")));
    }

    @Test
    public void invalidConfigTest() throws ConfigurationException {
        config = new TestCasesConfig("invalid.properties");
        assertThat(config.getTestCasesDirectory(), is(new File("cases")));
    }
}
