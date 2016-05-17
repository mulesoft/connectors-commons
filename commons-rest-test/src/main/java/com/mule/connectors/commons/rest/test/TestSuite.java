package com.mule.connectors.commons.rest.test;

import com.mule.connectors.commons.rest.test.config.TestCasesConfig;
import com.mule.connectors.commons.rest.test.provider.TestCasesProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TestSuite implements Runnable {

    @Override
    public void run() {

        // Load configuration.
        TestCasesConfig config = new TestCasesConfig("/cases-config.properties");

        // Read configuration from file.
        Map<String, TestCase> cases = new TestCasesProvider(config).getCases();

        // Execute request.
        Map<String, TestCaseResult> results = new HashMap<>();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new LoggingFilter(Logger.getLogger(getClass().getName()), true));
        Client client = ClientBuilder.newClient(clientConfig);

        for (Map.Entry<String, TestCase> entry : cases.entrySet()) {
            results.put(entry.getKey(), entry.getValue().execute(client));
        }

        // Present results.
        for (Map.Entry<String, TestCaseResult> entry : results.entrySet()) {
            System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue().isSuccessful()? "OK":"FAILED"));
            for (String errorMessage : entry.getValue().getErrorMessages()) {
                System.out.println(String.format("    %s", errorMessage));
            }
        }
    }
}
