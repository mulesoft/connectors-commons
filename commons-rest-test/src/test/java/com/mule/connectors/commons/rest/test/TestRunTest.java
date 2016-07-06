package com.mule.connectors.commons.rest.test;

import com.mule.connectors.commons.rest.test.provider.TestCase;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.ws.rs.client.Client;

import static org.easymock.EasyMock.*;

public class TestRunTest {

    @Test
    public void runTestTest() {
        TestCase testCase = mock(TestCase.class);
        Client client = mock(Client.class);
        testCase.execute(same(client));
        EasyMock.expectLastCall();
        replay(testCase, client);
        new TestRun("PATH", testCase, client).run();
        verify(testCase, client);
    }
}
