package com.yarkin;

import com.yarkin.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server("localhost", 3000);
        try {
            server.listen("/api/test"); // accept()...
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
