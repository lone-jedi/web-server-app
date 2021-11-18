package com.yarkin.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {
    private Response response;

    @BeforeEach
    public void before() {
        response = new Response();
    }

    @Test
    public void constructSuccessResponseAndGetHttp() {
        response.setStatus(200);
        response.setContent("Hello there", ContentType.TEXT);
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
