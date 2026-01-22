package com.github.appreciated.vortex_crud.demo.devplatform.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GitServiceTest {

    private MockWebServer mockWebServer;
    private GitService gitService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        // Remove trailing slash as WebClient + uriBuilder might behave differently?
        // Actually webClient.baseUrl(url) handles it.
        // But let's keep it simple.

        gitService = new GitService(
                WebClient.builder(),
                baseUrl,
                "user",
                "password"
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void initRepository() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse().setResponseCode(201));

        gitService.initRepository("test-repo");

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        // MockWebServer url() returns full URL.
        // request.getPath() returns path relative to server root.
        assertEquals("/api/v2/repositories", request.getPath());

        String body = request.getBody().readUtf8();
        assertTrue(body.contains("\"name\":\"test-repo\""));
        assertTrue(body.contains("\"namespace\":\"demo\""));
    }

    @Test
    void listFiles() throws InterruptedException {
        String jsonResponse = """
                {
                  "revision": "HEAD",
                  "children": [
                    {"name": "README.md", "path": "README.md", "directory": false},
                    {"name": "src", "path": "src", "directory": true}
                  ]
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        List<GitService.FileEntry> files = gitService.listFiles("test-repo", "");

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/api/v2/repositories/demo/test-repo/sources/HEAD/", request.getPath());

        assertEquals(2, files.size());
        assertEquals("README.md", files.get(0).name());
        assertFalse(files.get(0).isDirectory());
        assertEquals("src", files.get(1).name());
        assertTrue(files.get(1).isDirectory());
    }

    @Test
    void getFileContent() throws InterruptedException {
        String content = "Hello World";
        mockWebServer.enqueue(new MockResponse()
                .setBody(content)
                .addHeader("Content-Type", "text/plain"));

        String result = gitService.getFileContent("test-repo", "README.md");

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/api/v2/repositories/demo/test-repo/content/HEAD/README.md", request.getPath());

        assertEquals("Hello World", result);
    }
}
