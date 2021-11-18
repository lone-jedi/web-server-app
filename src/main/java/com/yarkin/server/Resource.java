package com.yarkin.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resource {
    private final String webResourcesPath = "src/main/resources/web-app";
    private final Pattern extensionPattern = Pattern.compile("\\.\\w+");
    private final String path;
    private HashMap<String, ContentType> extensions = new HashMap<>();

    {
        extensions.put(".html", ContentType.HTML);
        extensions.put(".css", ContentType.CSS);
        extensions.put(".js", ContentType.JS);
        extensions.put(".json", ContentType.JSON);
        extensions.put(".txt", ContentType.TEXT);
        extensions.put(".png", ContentType.PNG);
        extensions.put(".jpeg", ContentType.JPEG);
    }

    public Resource(String path) {
        this.path = path;
    }

    public boolean hasExtension() {
        Matcher extensionMatcher = extensionPattern.matcher(path);
        return extensionMatcher.find();
    }

    public ContentType getContentType() {
        Matcher extensionMatcher = extensionPattern.matcher(path);
        String extension = extensionMatcher.find() ?
                path.substring(extensionMatcher.start(), extensionMatcher.end()) : ".html";

        return extensions.get(extension);
    }

    public int load(byte[] bytes) throws IOException {
        String pathToResource = webResourcesPath + path;
        if(!hasExtension()) {
            pathToResource += "/index.html";
        }
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(pathToResource));
        int bytesCount = input.read(bytes);
        input.close();
        return bytesCount;
    }
}
