package com.yarkin.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {
    private String requestString;
    Request request;
    @BeforeEach
    public void before() {
        requestString = "GET / HTTP/1.1\n" +
                "Host: localhost:3000\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Sec-Fetch-Dest: document\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-Site: none\n" +
                "Sec-Fetch-User: ?1\n\nHello there";
        request = new Request(requestString);
    }

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
                        "Connection: close\n";
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
                "Connection: close\n";
        Request request = new Request(httpRequest);

        assertEquals("/", request.getPath());
    }

    @Test
    public void getPathFromHttpRequestWithoutBody() {
        String requestString= "GET / HTTP/1.1\n" +
                "Host: localhost:3000\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Sec-Fetch-Dest: document\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-Site: none\n" +
                "Sec-Fetch-User: ?1\n";

        Request request = new Request(requestString);

        assertEquals("/", request.getPath());
    }

    @Test
    public void getHeadersFromHttpRequestWithBody() {
        String expected = "GET / HTTP/1.1\n" +
                "Host: localhost:3000\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Sec-Fetch-Dest: document\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-Site: none\n" +
                "Sec-Fetch-User: ?1\n";
        assertEquals(expected, request.getHeaders());
    }

    @Test
    public void getSplitHeadersFromHttpRequestWithoutBody() {
        requestString= "GET / HTTP/1.1\n" +
                "Host: localhost:3000\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Sec-Fetch-Dest: document\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-Site: none\n" +
                "Sec-Fetch-User: ?1\n";

        request = new Request(requestString);

        String[] expected = {
                "GET / HTTP/1.1" ,
                "Host: localhost:3000" ,
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0" ,
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8" ,
                "Accept-Language: en-US,en;q=0.5" ,
                "Accept-Encoding: gzip, deflate" ,
                "Connection: keep-alive" ,
                "Upgrade-Insecure-Requests: 1" ,
                "Sec-Fetch-Dest: document" ,
                "Sec-Fetch-Mode: navigate" ,
                "Sec-Fetch-Site: none" ,
                "Sec-Fetch-User: ?1"
        };

        String headers = request.getHeaders();
        assertArrayEquals(expected, request.getSplitHeaders(headers));
    }

    @Test
    public void getSplitHeadersFromHttpRequestWithBody() {
        String[] expected = {
                "GET / HTTP/1.1" ,
                "Host: localhost:3000" ,
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0" ,
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8" ,
                "Accept-Language: en-US,en;q=0.5" ,
                "Accept-Encoding: gzip, deflate" ,
                "Connection: keep-alive" ,
                "Upgrade-Insecure-Requests: 1" ,
                "Sec-Fetch-Dest: document" ,
                "Sec-Fetch-Mode: navigate" ,
                "Sec-Fetch-Site: none" ,
                "Sec-Fetch-User: ?1"
        };

        String headers = request.getHeaders();
        assertArrayEquals(expected, request.getSplitHeaders(headers));
    }

    @Test
    public void getBodyFromHttpRequest() {
        assertEquals("Hello there", request.getBody());
    }

    @Test
    public void getBodyFromHttpRequestWithoutBody() {
        requestString= "GET / HTTP/1.1\n" +
                "Host: localhost:3000\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
                "Accept-Language: en-US,en;q=0.5\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Sec-Fetch-Dest: document\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-Site: none\n" +
                "Sec-Fetch-User: ?1\n";

        request = new Request(requestString);

        assertEquals(null, request.getBody());
    }
}
