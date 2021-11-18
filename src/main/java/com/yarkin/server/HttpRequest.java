package com.yarkin.server;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
    private static final Pattern pathPattern = Pattern.compile("/[\\w\\d\\./-]*");
    private static final Pattern extensionPattern = Pattern.compile("\\.\\w+");
    private HashMap<String, ContentType> extensions = new HashMap<>();
    private final String requestText;
    private String queryUri;
    private String[] headers;
    private String body;

    {
        extensions.put(".html", ContentType.HTML);
        extensions.put(".css", ContentType.CSS);
        extensions.put(".js", ContentType.JS);
        extensions.put(".json", ContentType.JSON);
        extensions.put(".txt", ContentType.TEXT);
        extensions.put(".png", ContentType.PNG);
        extensions.put(".jpeg", ContentType.JPEG);
    }

    public HttpRequest(String requestText) {
        this.requestText = requestText;
        body = getBody();

        String headersContent = getHeaders();
        headers = getSplitHeaders(headersContent);
        if(headers.length == 0) {
            throw new IllegalStateException("Request not correspond to HTTP format. Headers not found");
        }
        if(!headers[0].contains("HTTP")) {
            throw new IllegalStateException("Request not correspond to HTTP format. Missing HTTP header");
        }
        queryUri = parseQueryUri(headers[0]);
    }

    public static boolean hasExtension(String resourcePath) {
        Matcher extensionMatcher = extensionPattern.matcher(resourcePath);
        return extensionMatcher.find();
    }

    public ContentType getContentType() {
        Matcher extensionMatcher = extensionPattern.matcher(queryUri);
        String extension = extensionMatcher.find() ?
                queryUri.substring(extensionMatcher.start(), extensionMatcher.end()) : ".html";

        return extensions.get(extension);
    }

    public String getQueryUri() throws IllegalStateException {
        return queryUri;
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

    private String parseQueryUri(String httpHeader) {
        Matcher pathMatcher = pathPattern.matcher(httpHeader);
        if(!pathMatcher.find()) {
            throw new IllegalStateException("Request not correspond to HTTP format");
        }

        String result = httpHeader.substring(pathMatcher.start(), pathMatcher.end());
        if(!hasExtension(result)) {
            result += result.equals("/") ? "index.html" : "/index.html";
        }
        return result;
    }
}
