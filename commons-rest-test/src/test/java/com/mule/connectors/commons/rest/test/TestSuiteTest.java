package com.mule.connectors.commons.rest.test.provider;

import com.mule.connectors.commons.rest.test.TestSuite;
import org.junit.Test;

public class TestSuiteTest {

    @Test
    public void testRun() {
        // Runs the entire suite and validates no exceptions are thrown.
        new TestSuite().run();
    }
}
