package com.yarkin;

import com.yarkin.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(3000);
        server.setWebAppPath("src/main/resources/web-app");
        try {
            server.listen();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
