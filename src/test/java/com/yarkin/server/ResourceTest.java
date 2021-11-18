package com.yarkin.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceTest {
    @Test
    public void getContentTypeFromPath() {
        String path = "/api/test/v1/page.html";
        Resource resource = new Resource(path);

        ContentType expected = ContentType.HTML;
        ContentType actual = resource.getContentType();
        assertEquals(expected, actual);
    }
}
