package com.mule.connectors.commons.rest.test.provider;

import com.mule.connectors.commons.rest.test.config.TestCasesConfig;
import com.mule.connectors.commons.rest.test.exception.InvalidTestCaseFormatException;
import com.mule.connectors.commons.rest.test.exception.TestCasesDirectoryNotFoundException;
import com.mule.connectors.commons.rest.test.exception.UnexpectedParsingException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.collect.Iterables.all;
import static org.easymock.EasyMock.*;

public class TestCasesProviderTest {

    private TestCasesConfig config;

    @Before
    public void setup() {
        config = mock(TestCasesConfig.class);
    }

    @Test(expected = TestCasesDirectoryNotFoundException.class)
    public void getInexistingTestCasesFolderTest() {
        getCases("inexistent");
    }

    @Test(expected = TestCasesDirectoryNotFoundException.class)
    public void getNonDirectoryTestCasesFolderTest() {
        getCases("src/test/resources/cases/non_directory");
    }

    @Test
    public void getEmptyTestCasesFolderTest() {
        getCases("src/test/resources/cases/empty");
    }

    @Test(expected = InvalidTestCaseFormatException.class)
    public void getInvalidParsingTest() {
        getCases("src/test/resources/cases/invalid_parsing");
    }

    @Test(expected = UnexpectedParsingException.class)
    public void getInvalidMappingExceptionTest() {
        File casesDirectory = new File("src/test/resources/cases/invalid_parsing");
        File file = new File(casesDirectory, "test.case");
        file.setReadable(false);
        try {
            getCases(casesDirectory.getAbsolutePath());
        } finally {
            file.setReadable(true);
        }
    }

    @Test
    public void getCasesTest() {
        getCases("src/test/resources/cases/ok");
    }

    private void getCases(String path) {
        expect(config.getTestCasesDirectory()).andReturn(new File(path)).times(2);
        replay(config);
        List<Object[]> cases = new TestCasesProvider(config).getCases();
        verify(config);
        all(cases, alwaysTrue());
    }
}
