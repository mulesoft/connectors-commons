package com.mule.connectors.commons.rest.test.provider;

import java.io.File;
import java.io.FileFilter;

/**
 * {@link FileFilter} that checks that the File is not a directory and has the .case extension.
 */
public class TestCaseFileFilter implements FileFilter {

    @Override public boolean accept(File file) {
        return file.isFile() && file.getName().endsWith(".case");
    }
}
