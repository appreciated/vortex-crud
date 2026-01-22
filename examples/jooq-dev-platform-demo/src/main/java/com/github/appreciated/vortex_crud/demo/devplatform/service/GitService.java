package com.github.appreciated.vortex_crud.demo.devplatform.service;

import com.gitblit.models.PathModel;
import com.gitblit.utils.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitService {

    private static final String REPO_ROOT = "repositories";

    public void initRepository(String slug) {
        File repoDir = new File(REPO_ROOT, slug);
        if (!repoDir.exists()) {
            try {
                Git.init().setDirectory(repoDir).call();
                createDummyFiles(repoDir);
            } catch (Exception e) {
                throw new RuntimeException("Failed to init repository", e);
            }
        }
    }

    private void createDummyFiles(File repoDir) throws Exception {
         try (Git git = Git.open(repoDir)) {
            File readme = new File(repoDir, "README.md");
            Files.writeString(readme.toPath(), "# Demo Repository\n\nThis is a sample repository.");
            git.add().addFilepattern("README.md").call();

            File srcDir = new File(repoDir, "src");
            srcDir.mkdirs();
            File main = new File(srcDir, "Main.java");
            Files.writeString(main.toPath(), "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n}");
            git.add().addFilepattern("src/Main.java").call();

            git.commit().setMessage("Initial commit").call();
         }
    }

    public List<FileEntry> listFiles(String slug, String path) {
        File repoDir = new File(REPO_ROOT, slug);
        List<FileEntry> files = new ArrayList<>();

        if (!repoDir.exists()) {
             return files;
        }

        try (Git git = Git.open(repoDir)) {
            Repository repository = git.getRepository();
            RevCommit commit = JGitUtils.getCommit(repository, "HEAD");
            if (commit == null) return files;

            List<PathModel> paths = JGitUtils.getFilesInPath(repository, path, commit);
            for (PathModel p : paths) {
                files.add(new FileEntry(
                    p.name,
                    p.path,
                    p.isTree()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public String getFileContent(String slug, String path) {
         File repoDir = new File(REPO_ROOT, slug);
         if (!repoDir.exists()) return null;

         try (Git git = Git.open(repoDir)) {
            Repository repository = git.getRepository();
            RevCommit commit = JGitUtils.getCommit(repository, "HEAD");
            if (commit == null) return null;

            return JGitUtils.getStringContent(repository, commit.getTree(), path, StandardCharsets.UTF_8.name());
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
    }

    public record FileEntry(String name, String path, boolean isDirectory) {}
}
