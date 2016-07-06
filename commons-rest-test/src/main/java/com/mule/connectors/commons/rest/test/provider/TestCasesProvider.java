package com.mule.connectors.commons.rest.test.provider;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.mule.connectors.commons.rest.test.config.TestCasesConfig;
import com.mule.connectors.commons.rest.test.exception.TestCasesDirectoryNotFoundException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import javax.ws.rs.client.ClientBuilder;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class in charge of loading the {@link TestCase}s from the directory defined on the {@link TestCasesConfig}.
 */
public class TestCasesProvider {

    private final TestCasesConfig config;
    private final FileToCaseTransformer transformationStrategy;

    public TestCasesProvider(TestCasesConfig config) {
        this.config = config;
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new LoggingFilter(java.util.logging.Logger.getLogger(getClass().getSimpleName()), true));
        transformationStrategy = new FileToCaseTransformer(config.getTestCasesDirectory(), ClientBuilder.newClient(clientConfig));

    }

    public List<Object[]> getCases() {
        File testCasesDirectory = config.getTestCasesDirectory();
        if (testCasesDirectory.exists() && testCasesDirectory.isDirectory()) {
            return Lists.transform(getCaseFiles(testCasesDirectory), transformationStrategy);
        } else {
            throw new TestCasesDirectoryNotFoundException(testCasesDirectory.getAbsolutePath());
        }
    }

    private List<File> getCaseFiles(File directory) {
        List<File> files = new ArrayList<>();
        for (File subDirectory : filter(directory, new DirectoryFilter())) {
            files.addAll(getCaseFiles(subDirectory));
        }
        files.addAll(Arrays.asList(filter(directory, new TestCaseFileFilter())));
        return files;
    }

    private File[] filter(File directory, FileFilter filter) {
        return Optional.fromNullable(directory.listFiles(filter)).or(new File[] {});
    }
}
