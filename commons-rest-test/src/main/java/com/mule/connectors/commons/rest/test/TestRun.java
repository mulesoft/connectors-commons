package com.mule.connectors.commons.rest.test;

import com.mule.connectors.commons.rest.test.config.TestCasesConfig;
import com.mule.connectors.commons.rest.test.provider.TestCase;
import com.mule.connectors.commons.rest.test.provider.TestCasesProvider;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestRun implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(TestRun.class);

    private final TestCase testCase;
    private final Client client;

    public TestRun(String path, TestCase testCase, Client client) {
        logger.info("Running test on '{}'.", path);
        this.testCase = testCase;
        this.client = client;
    }

    @Parameterized.Parameters(name = "Test case for \"{0}\"")
    public static Collection<Object[]> getCases() throws ConfigurationException {
        return new TestCasesProvider(new TestCasesConfig("src/test/resources/cases-config.properties")).getCases();
    }

    @Test
    @Override
    public void run() {
        testCase.execute(client);
    }
}
