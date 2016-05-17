package com.mule.connectors.commons.rest.test.config;

import com.mule.connectors.commons.rest.test.exception.PropertiesFileException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class TestCasesConfigTest {

    private TestCasesConfig config;

    @Test
    public void getTestCasesDirectoryTest() {
        config = new TestCasesConfig("/cases-config.properties");
        assertEquals(new File("src/test/resources/cases/ok"), config.getTestCasesDirectory());
    }

    @Test
    public void getDefaultTestCasesDirectoryTest() {
        config = new TestCasesConfig("/inexistent.properties");
        assertEquals(new File("cases"), config.getTestCasesDirectory());
    }

    @Test(expected = PropertiesFileException.class)
    public void invalidConfigTest() {
        config = new TestCasesConfig("/invalid.properties");
        assertEquals(new File("cases"), config.getTestCasesDirectory());
    }

    @Test(expected = PropertiesFileException.class)
    public void inputStreamFailureTest() {
        config = new TestCasesConfig("/invalid.properties") {

            @Override protected InputStream openStream(String propertiesFile) throws IOException {
                throw new IOException("Fail");
            }
        };
    }

    @Test(expected = PropertiesFileException.class)
    public void inputStreamLoadFailureTest() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        expect(inputStream.read((byte[]) anyObject())).andThrow(new IOException("Fail"));
        replay(inputStream);
        config = new TestCasesConfig("/invalid.properties") {

            @Override protected InputStream openStream(String propertiesFile) throws IOException {
                return inputStream;
            }
        };
    }

    @Test(expected = PropertiesFileException.class)
    public void defaultInputStreamLoadFailureTest() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        expect(inputStream.read((byte[]) anyObject())).andThrow(new IOException("Fail"));
        replay(inputStream);
        config = new TestCasesConfig("/invalid.properties") {

            @Override protected InputStream openStream(String propertiesFile) throws IOException {
                return "/invalid.properties".equals(propertiesFile)? null : inputStream;
            }
        };
    }

    @Test(expected = PropertiesFileException.class)
    public void defaultInputStreamFailureTest() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        expect(inputStream.read((byte[]) anyObject())).andThrow(new IOException("Fail"));
        replay(inputStream);
        config = new TestCasesConfig("/invalid.properties") {

            @Override protected InputStream openStream(String propertiesFile) throws IOException {
                if ("/invalid.properties".equals(propertiesFile)) {
                    return null;
                } else {
                    throw new IOException("Fail");
                }
            }
        };
    }
}
