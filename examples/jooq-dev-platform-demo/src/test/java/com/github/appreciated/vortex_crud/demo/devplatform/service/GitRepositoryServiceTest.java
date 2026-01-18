package com.github.appreciated.vortex_crud.demo.devplatform.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GitRepositoryServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void testInitAndListFiles() {
        GitRepositoryService service = new GitRepositoryService(tempDir.toFile());
        String repoName = "test-repo";

        service.initRepository(repoName);

        List<GitRepositoryService.FileEntry> files = service.listFilesAtRef(repoName, null, "");
        assertFalse(files.isEmpty());
        assertTrue(files.stream().anyMatch(f -> f.name().equals("README.md")));

        String content = service.getFileContent(repoName, null, "README.md");
        assertNotNull(content);
        assertTrue(content.contains(repoName));
    }
}
