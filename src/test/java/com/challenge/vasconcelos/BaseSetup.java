package com.challenge.vasconcelos;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.challenge.vasconcelos.model.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseSetup {
    protected static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    protected Asset assetResponse;

    protected String readTestFile(String path) throws IOException, URISyntaxException {
        return Files.readString(Paths.get(ClassLoader.getSystemResource(path).toURI()));
    }
}
