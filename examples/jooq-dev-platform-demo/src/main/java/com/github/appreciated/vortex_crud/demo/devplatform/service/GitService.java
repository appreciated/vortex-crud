package com.github.appreciated.vortex_crud.demo.devplatform.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
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
            ObjectId lastCommitId = repository.resolve("HEAD");
            if (lastCommitId == null) return files;

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(lastCommitId);
                RevTree tree = commit.getTree();

                if (path == null || path.isEmpty() || path.equals("/")) {
                    try (TreeWalk treeWalk = new TreeWalk(repository)) {
                        treeWalk.addTree(tree);
                        treeWalk.setRecursive(false);
                        while (treeWalk.next()) {
                            files.add(new FileEntry(
                                treeWalk.getNameString(),
                                treeWalk.getPathString(),
                                treeWalk.isSubtree()
                            ));
                        }
                    }
                } else {
                    try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree)) {
                        if (treeWalk != null && treeWalk.isSubtree()) {
                             treeWalk.enterSubtree();
                             while (treeWalk.next()) {
                                 files.add(new FileEntry(
                                     treeWalk.getNameString(),
                                     treeWalk.getPathString(),
                                     treeWalk.isSubtree()
                                 ));
                             }
                        }
                    }
                }
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
            ObjectId lastCommitId = repository.resolve("HEAD");
            if (lastCommitId == null) return null;

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(lastCommitId);
                RevTree tree = commit.getTree();

                try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree)) {
                    if (treeWalk == null) return null;

                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader loader = repository.open(objectId);

                    return new String(loader.getBytes(), StandardCharsets.UTF_8);
                }
            }
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
    }

    public record FileEntry(String name, String path, boolean isDirectory) {}
}
