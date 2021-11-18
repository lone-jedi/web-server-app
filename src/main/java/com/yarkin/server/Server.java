package com.yarkin.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int DEFAULT_PORT = 3000;
    private Url url;

    public Server(String hostName) {
        this(hostName, DEFAULT_PORT);
    }

    public Server(String hostName, int port) {
        url = new Url(hostName, port);
    }

    public Server(Url url) {
        this.url = url;
    }

    public void listen(String path) throws IOException {
        Resource resource = new Resource(path);
        ServerSocket serverSocket = new ServerSocket(url.getPort());
        // start web server I\O and accept()
        while(true) {
            try (Socket socket = serverSocket.accept();
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(
                         new OutputStreamWriter(socket.getOutputStream()));) {
                // Receive data from server
                char[] inputBytes = new char[32 * 1024]; // 32KB
                int countOfBytes = reader.read(inputBytes);
                String requestText = new String(inputBytes, 0, countOfBytes);

                // Parse client request
                Request request = new Request(requestText);
                Response response = new Response();

                // check if at the path exists index resource
                if(!request.getPath().equals(path) || !resource.isExists()) {
                    response.setStatus(404);
                    writer.write(response.getHttp());
                    continue;
                }

                // load index file
                String content = resource.loadIndex();

                // write to outputStream & again ->
                response.setStatus(200);
                writer.write(response.getHttp());
            }
        }



    }
}
