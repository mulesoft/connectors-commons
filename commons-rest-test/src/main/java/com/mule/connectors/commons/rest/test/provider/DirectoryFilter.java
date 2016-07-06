package com.mule.connectors.commons.rest.test.provider;

import java.io.File;
import java.io.FileFilter;

/**
 * {@link FileFilter} that checks that the {@link File} is a directory.
 */
public class DirectoryFilter implements FileFilter {

    @Override public boolean accept(File file) {
        return file.isDirectory();
    }
}
