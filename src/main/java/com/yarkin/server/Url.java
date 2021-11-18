package com.yarkin.server;

public class Url {
    private final int port;
    private final String host;

    public Url(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}
