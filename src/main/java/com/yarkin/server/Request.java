package com.yarkin.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private final String requestText;
    private String path;
    private String[] headers;
    private String body;
    private final Pattern pathPattern = Pattern.compile("/[\\w\\d\\./-]*");

    public Request(String requestText) {
        this.requestText = requestText;
        String[] parts = requestText.split("\n\n", 2);
        if(parts.length != 2) {
            throw new IllegalStateException("Request not correspond to HTTP format. Headers & body not founs");
        }

        headers = parts[0].split("\n");
        if(headers.length == 0) {
            throw new IllegalStateException("Request not correspond to HTTP format. Headers not found");
        }

        if(!headers[0].contains("HTTP")) {
            throw new IllegalStateException("Request not correspond to HTTP format. Missing HTTP header");
        }

        body = parts[1];
    }

    public String getPath() throws IllegalStateException {
        if(path == null) {
            parsePath();
        }
        return path;
    }

    private void parsePath() throws IllegalStateException {
        Matcher pathMatcher = pathPattern.matcher(headers[0]);
        if(!pathMatcher.find()) {
            throw new IllegalStateException("Request not correspond to HTTP format");
        }
        path = headers[0].substring(pathMatcher.start(), pathMatcher.end());
    }
}
