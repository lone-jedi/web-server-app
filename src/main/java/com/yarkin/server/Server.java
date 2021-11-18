package com.yarkin.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int DEFAULT_PORT = 3000;
    private String pathToWebApp;
    private Url url;

    public Server(String hostName) {
        this(hostName, DEFAULT_PORT);
    }

    public Server(String hostName, int port) {
        url = new Url(hostName, port);
    }

    public void listen() throws IOException {
        ServerSocket serverSocket = new ServerSocket(url.getPort());

        while(true) {
            try (Socket socket = serverSocket.accept();
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(
                         new OutputStreamWriter(socket.getOutputStream()));) {
                // Obtain data from client
                char[] inputBytes = new char[32 * 1024]; // 32KB
                int countOfBytes = reader.read(inputBytes);
                String requestText = countOfBytes >= 0 ? new String(inputBytes, 0, countOfBytes) : "";

                // Parse client request
                HttpRequest request = new HttpRequest(requestText);
                HttpResponse response = new HttpResponse();

                // check if request path contains required
                String pathToResource = pathToWebApp + request.getQueryUri();
                Resource resource = new Resource(pathToResource);
                if(!new File(pathToResource).exists()) {
                    response.setStatus(404);
                    response.setContentType(ContentType.HTML);
                    response.setContent("<h1>404 Not Found</h1>");
                    writer.write(response.getHttp());
                    continue;
                }

                // get file content
                byte[] content = new byte[1024 * 1024 * 4]; // 4 MB
                int bytesCount = resource.load(content);

                // write to outputStream
                ContentType contentType = request.getContentType();
                response.setStatus(200);
                response.setContentType(contentType);

                if(bytesCount <= 0) {
                    writer.write(response.getHttp());
                    continue;
                }

                if(contentType.equals(ContentType.PNG) || contentType.equals(ContentType.JPEG)) {
                    writer.write(response.getHttp());
                    writer.write("\n");
                    socket.getOutputStream().write(content, 0, bytesCount);
                    continue;
                }

                response.setContent(new String(content, 0, bytesCount));
                writer.write(response.getHttp());
            } catch(IllegalStateException e) {
                e.printStackTrace();
            } catch(Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void setWebAppPath(String pathToWebApp) {
        this.pathToWebApp = pathToWebApp;
    }
}
