package com.github.appreciated.vortex_crud.demo.devplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitService {

    private static final Logger log = LoggerFactory.getLogger(GitService.class);
    private final WebClient webClient;
    private final String scmUrl;

    public GitService(WebClient.Builder webClientBuilder,
                      @Value("${scm.url}") String scmUrl,
                      @Value("${scm.username}") String username,
                      @Value("${scm.password}") String password) {
        this.scmUrl = scmUrl;
        this.webClient = webClientBuilder
                .baseUrl(scmUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    public void initRepository(String slug) {
        // Create repository in SCM-Manager
        // POST /api/v2/repositories
        try {
            webClient.post()
                .uri("/api/v2/repositories")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateRepositoryDto("demo", slug, "git"))
                .retrieve()
                .toBodilessEntity()
                .block();
            log.info("Repository {} created successfully.", slug);

            // Note: Original implementation created dummy files.
            // SCM-Manager API requires separate calls or git client to commit files.
            // Skipping file creation for now.
            log.warn("Skipping dummy file creation for repository {} as it requires additional API calls.", slug);

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                log.info("Repository {} already exists.", slug);
            } else {
                log.error("Failed to create repository {}", slug, e);
                throw new RuntimeException("Failed to create repository " + slug, e);
            }
        } catch (Exception e) {
            log.error("Failed to create repository {}", slug, e);
            throw new RuntimeException("Failed to create repository " + slug, e);
        }
    }

    public List<FileEntry> listFiles(String slug, String path) {
        // GET /api/v2/repositories/demo/{slug}/sources/HEAD/{path}
        String actualPath = (path == null || path.isEmpty() || path.equals("/")) ? "" : path;

        try {
            ScmSourceResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/v2/repositories/demo/{slug}/sources/HEAD/{path}")
                    .build(slug, actualPath))
                .retrieve()
                .bodyToMono(ScmSourceResponse.class)
                .block();

            if (response != null && response.children() != null) {
                return response.children().stream()
                    .map(f -> new FileEntry(f.name(), f.path(), f.directory()))
                    .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Failed to list files for repository {} path {}", slug, path, e);
        }
        return new ArrayList<>();
    }

    public String getFileContent(String slug, String path) {
        try {
             return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/v2/repositories/demo/{slug}/content/HEAD/{path}")
                    .build(slug, path))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            log.error("Failed to get content for repository {} path {}", slug, path, e);
            return null;
        }
    }

    public record FileEntry(String name, String path, boolean isDirectory) {}

    // DTOs
    public record CreateRepositoryDto(String namespace, String name, String type) {}
    public record ScmSourceResponse(String revision, List<ScmFile> children) {}
    public record ScmFile(String name, String path, boolean directory) {}
}
