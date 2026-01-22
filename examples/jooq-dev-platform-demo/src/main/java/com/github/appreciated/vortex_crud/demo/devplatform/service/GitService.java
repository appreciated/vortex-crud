package com.github.appreciated.vortex_crud.demo.devplatform.service;

import sonia.scm.client.ScmClient;
import sonia.scm.client.ScmClientSession;
import sonia.scm.client.RepositoryClientHandler;
import sonia.scm.client.ClientRepositoryBrowser;
import sonia.scm.client.FileObjectWrapper;
import sonia.scm.repository.Repository;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GitService {

    private static final String SCM_URL = "http://localhost:8080/scm";
    private static final String SCM_USER = "scmadmin";
    private static final String SCM_PASSWORD = "scmadmin";

    public void initRepository(String slug) {
        try (ScmClientSession session = ScmClient.createSession(SCM_URL, SCM_USER, SCM_PASSWORD)) {
            RepositoryClientHandler repositoryHandler = session.getRepositoryHandler();
            List<Repository> repositories = repositoryHandler.getAll();
            boolean exists = repositories.stream()
                    .anyMatch(r -> "git".equals(r.getType()) && slug.equals(r.getName()));

            if (!exists) {
                Repository repository = new Repository();
                repository.setName(slug);
                repository.setType("git");
                repositoryHandler.create(repository);
                System.out.println("Repository " + slug + " created. Please push content to " + SCM_URL + "/git/" + slug);
            }
        } catch (Exception e) {
            // Log and ignore to allow demo to start without SCM-Manager
            System.err.println("Failed to init repository: " + e.getMessage());
        }
    }

    public List<FileEntry> listFiles(String slug, String path) {
        try (ScmClientSession session = ScmClient.createSession(SCM_URL, SCM_USER, SCM_PASSWORD)) {
            RepositoryClientHandler repositoryHandler = session.getRepositoryHandler();
            Repository repository = repositoryHandler.get("git", slug);
            if (repository == null) {
                return Collections.emptyList();
            }

            ClientRepositoryBrowser browser = repositoryHandler.getRepositoryBrowser(repository);
            // Assuming default revision (HEAD)
            List<FileObjectWrapper> files = browser.getFiles(path != null && !path.isEmpty() ? path : "/");

            List<FileEntry> entries = new ArrayList<>();
            if (files != null) {
                for (FileObjectWrapper file : files) {
                    entries.add(new FileEntry(file.getName(), file.getPath(), file.isDirectory()));
                }
            }
            return entries;
        } catch (Exception e) {
             System.err.println("Failed to list files: " + e.getMessage());
             return Collections.emptyList();
        }
    }

    public String getFileContent(String slug, String path) {
        try (ScmClientSession session = ScmClient.createSession(SCM_URL, SCM_USER, SCM_PASSWORD)) {
            RepositoryClientHandler repositoryHandler = session.getRepositoryHandler();
            Repository repository = repositoryHandler.get("git", slug);
            if (repository == null) {
                return null;
            }

            ClientRepositoryBrowser browser = repositoryHandler.getRepositoryBrowser(repository);
            try (InputStream stream = browser.getContent("HEAD", path)) {
                 if (stream == null) return null;
                 return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            System.err.println("Failed to get file content: " + e.getMessage());
            return null;
        }
    }

    public record FileEntry(String name, String path, boolean isDirectory) {}
}
