package com.mule.connectors.commons.rest.test.provider;

import java.io.File;
import java.io.FileFilter;

/**
 */
public class TestCaseFileFilter implements FileFilter {

    @Override public boolean accept(File file) {
        return file.isFile() && file.getName().endsWith(".case");
    }
}
