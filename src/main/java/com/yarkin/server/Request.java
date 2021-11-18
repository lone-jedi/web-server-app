package com.yarkin.server;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private static final Pattern pathPattern = Pattern.compile("/[\\w\\d\\./-]*");
    // private static final Pattern splitOnBodyAndHeadersPattern = Pattern.compile("\n\n");
    // private static final Pattern splitHeadersPattern = Pattern.compile("\n");

    private final String requestText;
    private String path;
    private String[] headers;
    private String body;

    public Request(String requestText) {
        this.requestText = requestText;
        body = getBody();

        String headersContent = getHeaders();
        String[] headers = getSplitHeaders(headersContent);
        if(headers.length == 0) {
            throw new IllegalStateException("Request not correspond to HTTP format. Headers not found");
        }
        if(!headers[0].contains("HTTP")) {
            throw new IllegalStateException("Request not correspond to HTTP format. Missing HTTP header");
        }
        this.headers = headers;
    }

    public String getPath() throws IllegalStateException {
        if(path == null) {
            Matcher pathMatcher = pathPattern.matcher(headers[0]);
            if(!pathMatcher.find()) {
                throw new IllegalStateException("Request not correspond to HTTP format");
            }
            path = headers[0].substring(pathMatcher.start(), pathMatcher.end());
        }
        return path;
    }

    public String getBody() {
        if(body == null) {
            String[] parts = requestText.split("\n\n", 2);
            if(parts.length == 2) {
                body = parts[1];
            }
        }
        return body;
    }

    public String getHeaders() {
        String[] parts = requestText.split("\n\n", 2);
        if(parts.length == 2) {
            return parts[0] + "\n";
        }

        if(parts.length == 1) {
            return parts[0];
        }

        return null;
    }

    public String[] getSplitHeaders(String headersContent) {
        return headersContent.split("\n");
    }
}
