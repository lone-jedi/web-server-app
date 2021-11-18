package com.yarkin.server;

public enum ContentType {
    HTML("text/html"),
    CSS("text/css"),
    TEXT("text/plain"),
    JS("application/javascript"), // text/javascript - Obsolete
    JSON("application/json"),
    PNG("image/png"),
    JPEG("image/jpeg");

    private String typeName;

    ContentType(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
