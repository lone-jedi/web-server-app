package com.yarkin.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpResponseTest {
    private HttpResponse response;

    @BeforeEach
    public void before() {
        response = new HttpResponse();
    }

    @Test
    public void constructSuccessResponseAndGetHttp() {
        response.setStatus(200);
        response.setContentType(ContentType.TEXT);
        response.setContent("Hello there");
        String expected = "HTTP/1.1 200 OK\nContent-Type: text/plain\n\n" +
                            "Hello there";
        String actual = response.getHttp();
        assertEquals(expected, actual);
    }

    @Test
    public void constructResponseWith404CodeAndGetHttp() {
        response.setStatus(404);
        String expected = "HTTP/1.1 404 Not Found\nContent-Type: text/html\n";
        String actual = response.getHttp();
        assertEquals(expected, actual);
    }
}
