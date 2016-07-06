package com.mule.connectors.commons.rest.test.provider;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.mule.connectors.commons.rest.test.exception.InvalidTestCaseFormatException;
import com.mule.connectors.commons.rest.test.exception.UnexpectedParsingException;

import javax.ws.rs.client.Client;
import java.io.File;
import java.io.IOException;

/**
 * {@link Function} that transforms test case files to {@link java.util.Map.Entry Entries} that can be used to test.
 */
public class FileToCaseTransformer implements Function<File, Object[]> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Client client;
    private final String baseDirectory;

    public FileToCaseTransformer(File baseDirectory, Client client) {
        this.client = client;
        this.baseDirectory = baseDirectory.getAbsolutePath();
    }

    @Override
    public Object[] apply(File file) {
        try {
            return new Object[] { file.getAbsolutePath().replace(baseDirectory, ""), objectMapper.readValue(file, TestCase.class), client };
        } catch (JsonParseException | JsonMappingException e) {
            throw new InvalidTestCaseFormatException(file, e);
        } catch (IOException e) {
            throw new UnexpectedParsingException(file, e);
        }
    }
}
