package com.mule.connectors.commons.rest.test.provider;

import com.mule.connectors.commons.rest.test.config.TestCasesConfig;
import com.mule.connectors.commons.rest.test.exception.InvalidTestCaseFormatException;
import com.mule.connectors.commons.rest.test.exception.NoTestCasesException;
import com.mule.connectors.commons.rest.test.exception.TestCasesDirectoryNotFoundException;
import com.mule.connectors.commons.rest.test.exception.UnexpectedParsingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static org.easymock.EasyMock.*;

public class TestCasesProviderTest {

    @Test(expected = TestCasesDirectoryNotFoundException.class)
    public void getInexistingTestCasesFolderTest() {
        TestCasesConfig config = mock(TestCasesConfig.class);
        expect(config.getTestCasesDirectory()).andReturn(new File("inexisting"));
        replay(config);
        new TestCasesProvider(config).getCases();
    }

    @Test(expected = TestCasesDirectoryNotFoundException.class)
    public void getNonDirectoryTestCasesFolderTest() {
        TestCasesConfig config = mock(TestCasesConfig.class);
        expect(config.getTestCasesDirectory()).andReturn(new File("src/test/resources/cases/non_directory"));
        replay(config);
        new TestCasesProvider(config).getCases();
    }

    @Test(expected = NoTestCasesException.class)
    public void getEmptyTestCasesFolderTest() {
        TestCasesConfig config = mock(TestCasesConfig.class);
        expect(config.getTestCasesDirectory()).andReturn(new File("src/test/resources/cases/empty"));
        replay(config);
        new TestCasesProvider(config).getCases();
    }


    @Test(expected = InvalidTestCaseFormatException.class)
    public void getInvalidParsingTest() {
        TestCasesConfig config = mock(TestCasesConfig.class);
        expect(config.getTestCasesDirectory()).andReturn(new File("src/test/resources/cases/invalid_parsing"));
        replay(config);
        new TestCasesProvider(config).getCases();
    }

    @Test(expected = UnexpectedParsingException.class)
    public void getInvalidMappingExceptionTest() {
        File casesDirectory = new File("src/test/resources/cases/invalid_parsing");
        File file = new File(casesDirectory, "test.case");
        file.setReadable(false);
        try {
            TestCasesConfig config = mock(TestCasesConfig.class);
            expect(config.getTestCasesDirectory()).andReturn(casesDirectory);
            replay(config);
            new TestCasesProvider(config).getCases();
        } finally {
            file.setReadable(true);
        }
    }

    @Test
    public void getCasesTest() {
        TestCasesConfig config = mock(TestCasesConfig.class);
        expect(config.getTestCasesDirectory()).andReturn(new File("src/test/resources/cases/ok"));
        replay(config);
        Assert.assertEquals(4, new TestCasesProvider(config).getCases().size());
    }
}
