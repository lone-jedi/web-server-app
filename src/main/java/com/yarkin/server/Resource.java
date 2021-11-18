package com.yarkin.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Resource {
    private final String pathToResource;

    public Resource(String pathToResource) {
        this.pathToResource = pathToResource;
    }

    public int load(byte[] bytes) throws IOException {
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(pathToResource));
        int bytesCount = input.read(bytes);
        input.close();
        return bytesCount;
    }
}
