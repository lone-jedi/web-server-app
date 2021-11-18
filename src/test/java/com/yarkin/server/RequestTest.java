package com.yarkin.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestTest {
    @Test
    public void getPathFromHttpRequest() {
        String httpRequest =
                "GET /api/v1/test?hello=world&id=2 HTTP/1.1\n" +
                "Host: ru.wikipedia.org\n" +
                "User-Agent: Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5\n" +
                "Accept: text/html\n" +
                "Connection: close\n\nbody";
        Request request = new Request(httpRequest);

        assertEquals("/api/v1/test", request.getPath());
    }

    @Test
    public void getPathFromNoHttpRequest() {
        String httpRequest =
                        "Host: ru.wikipedia.org\n" +
                        "User-Agent: Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5\n" +
                        "Accept: text/html\n" +
                        "Connection: close\n\n";
        assertThrows(IllegalStateException.class, () -> {
            new Request(httpRequest);
        });

        try {
            new Request(httpRequest);
        } catch (Throwable e) {
            assertEquals("Request not correspond to HTTP format. Missing HTTP header", e.getMessage());
        }
    }

    @Test
    public void getPathFromHttpRequestWithRootPath() {
        String httpRequest = "GET / HTTP/1.1\n" +
                        "Host: ru.wikipedia.org\n" +
                        "User-Agent: Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5\n" +
                        "Accept: text/html\n" +
                        "Connection: close\n\n";
        Request request = new Request(httpRequest);

        assertEquals("/", request.getPath());
    }
}
