package com.yarkin.server;

import java.util.HashMap;

public class Response {
    private ContentType contentType = ContentType.HTML;
    private int statusCode;
    private String content;
    private HashMap<Integer, String> statusCodes = new HashMap<>();

    {
        statusCodes.put(200, "OK");
        statusCodes.put(404, "Not Found");
    }

    public void setStatus(int status) {
        this.statusCode = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getHttp() {
        StringBuilder response = new StringBuilder("HTTP/1.1 ");
        response.append(statusCode + " ");
        response.append(statusCodes.get(statusCode));
        response.append("\n");
        response.append("Content-Type: ");
        response.append(contentType);
        response.append("\n");

        if(content != null) {
            response.append("\n");
            response.append(content);
        }
        return response.toString();
    }
}
