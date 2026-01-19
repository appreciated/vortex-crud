package com.github.appreciated.vortex_crud.demo.devplatform.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GitServiceTest {

    private GitService gitService;
    private static final String TEST_REPO_SLUG = "test-repo";
    private static final String REPO_ROOT = "repositories";

    @BeforeEach
    void setUp() {
        gitService = new GitService();
        // Ensure clean state
        deleteRepoDir();
    }

    @AfterEach
    void tearDown() {
        deleteRepoDir();
    }

    private void deleteRepoDir() {
        File repoDir = new File(REPO_ROOT);
        if (repoDir.exists()) {
            FileSystemUtils.deleteRecursively(repoDir);
        }
    }

    @Test
    void testInitRepository() {
        gitService.initRepository(TEST_REPO_SLUG);
        File repoDir = new File(REPO_ROOT, TEST_REPO_SLUG);
        assertTrue(repoDir.exists());
        assertTrue(new File(repoDir, ".git").exists());
        assertTrue(new File(repoDir, "README.md").exists());
    }

    @Test
    void testListBranches() {
        gitService.initRepository(TEST_REPO_SLUG);
        List<String> branches = gitService.listBranches(TEST_REPO_SLUG);
        assertTrue(branches.contains("master") || branches.contains("main"));
    }

    @Test
    void testCreateAndDeleteBranch() {
        gitService.initRepository(TEST_REPO_SLUG);
        String newBranch = "feature/test";
        gitService.createBranch(TEST_REPO_SLUG, newBranch, "HEAD");

        List<String> branches = gitService.listBranches(TEST_REPO_SLUG);
        assertTrue(branches.contains(newBranch));

        gitService.deleteBranch(TEST_REPO_SLUG, newBranch);
        branches = gitService.listBranches(TEST_REPO_SLUG);
        assertFalse(branches.contains(newBranch));
    }

    @Test
    void testFileContentAndHistory() throws Exception {
        gitService.initRepository(TEST_REPO_SLUG);

        // Check initial content
        String readme = gitService.getFileContent(TEST_REPO_SLUG, "README.md");
        assertNotNull(readme);
        assertTrue(readme.contains("Demo Repository"));

        // Check commit history
        List<GitService.CommitInfo> history = gitService.getCommitHistory(TEST_REPO_SLUG, "HEAD");
        assertFalse(history.isEmpty());
        assertEquals("Initial commit", history.get(0).shortMessage());

        // Check Blame
        List<GitService.BlameInfo> blame = gitService.blame(TEST_REPO_SLUG, "HEAD", "README.md");
        assertFalse(blame.isEmpty());
        assertEquals("Initial commit", blame.get(0).message());
    }
}
