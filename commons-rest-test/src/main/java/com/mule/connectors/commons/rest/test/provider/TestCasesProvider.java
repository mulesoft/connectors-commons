package com.mule.connectors.commons.rest.test.provider;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mule.connectors.commons.rest.test.TestCase;
import com.mule.connectors.commons.rest.test.config.TestCasesConfig;
import com.mule.connectors.commons.rest.test.exception.InvalidTestCaseFormatException;
import com.mule.connectors.commons.rest.test.exception.NoTestCasesException;
import com.mule.connectors.commons.rest.test.exception.TestCasesDirectoryNotFoundException;
import com.mule.connectors.commons.rest.test.exception.UnexpectedParsingException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCasesProvider {

    private final TestCasesConfig config;
    private ObjectMapper objectMapper = new ObjectMapper();

    public TestCasesProvider(TestCasesConfig config) {
        this.config = config;
    }

    public Map<String, TestCase> getCases() {
        File testCasesDirectory = config.getTestCasesDirectory();
        if (testCasesDirectory.exists() && testCasesDirectory.isDirectory()) {
            return getCases(testCasesDirectory);
        } else {
            throw new TestCasesDirectoryNotFoundException(testCasesDirectory.getAbsolutePath());
        }
    }

    private Map<String, TestCase> getCases(File directory) {
        Map<String, TestCase> cases = new HashMap<>();
        List<NoTestCasesException> suppressed = new ArrayList<>();
        for (File subDirectory : directory.listFiles(new DirectoryFilter())) {
            try {
                cases.putAll(getCases(subDirectory));
            } catch(NoTestCasesException e) {
                suppressed.add(e);
            }
        }
        File[] files = directory.listFiles(new TestCaseFileFilter());
        if (files.length == 0 && cases.isEmpty()) {
            NoTestCasesException exception = new NoTestCasesException(directory.getAbsolutePath());
            for (NoTestCasesException suppressedException : suppressed) {
                exception.addSuppressed(suppressedException);
            }
            throw exception;
        }
        for (File testCaseFile : files) {
            try {
                cases.put(testCaseFile.getAbsolutePath(), objectMapper.readValue(testCaseFile, TestCase.class));
            } catch (JsonParseException | JsonMappingException e) {
                throw new InvalidTestCaseFormatException(testCaseFile, e);
            } catch (IOException e) {
                throw new UnexpectedParsingException(testCaseFile, e);
            }
        }
        return cases;
    }
}
