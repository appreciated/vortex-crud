package com.github.appreciated.vortex_crud.demo.devplatform.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GitRepositoryService {

    private final File rootDir = new File("repositories");

    public GitRepositoryService() {
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }
    }

    public void initRepository(String name) {
        File repoDir = new File(rootDir, name);
        if (!repoDir.exists()) {
            try (Git git = Git.init().setDirectory(repoDir).call()) {
                // Create a dummy file to have a HEAD
                File readme = new File(repoDir, "README.md");
                if (readme.createNewFile()) {
                     java.nio.file.Files.writeString(readme.toPath(), "# " + name + "\n\nThis is a demo repository.");
                }

                // Add some Java structure if it's the demo repo
                if (name.contains("demo")) {
                    File srcDir = new File(repoDir, "src/main/java/com/example");
                    srcDir.mkdirs();
                    File mainClass = new File(srcDir, "Main.java");
                    if (mainClass.createNewFile()) {
                        java.nio.file.Files.writeString(mainClass.toPath(),
                            "package com.example;\n\npublic class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n}");
                    }

                    File pomFile = new File(repoDir, "pom.xml");
                    if (pomFile.createNewFile()) {
                        java.nio.file.Files.writeString(pomFile.toPath(),
                            "<project>\n    <modelVersion>4.0.0</modelVersion>\n    <groupId>com.example</groupId>\n    <artifactId>" + name + "</artifactId>\n    <version>1.0.0</version>\n</project>");
                    }
                }

                git.add().addFilepattern(".").call();
                git.commit().setMessage("Initial commit").call();

            } catch (GitAPIException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<FileEntry> listFilesAtRef(String repoName, String refName, String path) {
        File repoDir = new File(rootDir, repoName);
        if (!repoDir.exists()) {
             // For demo purposes, auto-init if not exists
             initRepository(repoName);
        }

        List<FileEntry> files = new ArrayList<>();
        try (Git git = Git.open(repoDir); Repository repository = git.getRepository()) {
            ObjectId commitId = repository.resolve(refName != null ? refName : Constants.HEAD);
            if (commitId == null) {
                return Collections.emptyList();
            }

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                RevTree tree = commit.getTree();

                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(false);

                    if (path != null && !path.isEmpty() && !path.equals("/")) {
                        // We need to walk to the path
                        // Simple approach: split path and navigate
                        // Or use PathFilter to find the folder, then enter it.
                        // But standard TreeWalk iteration is easier.

                        String[] parts = path.split("/");
                        for (String part : parts) {
                             boolean found = false;
                             while (treeWalk.next()) {
                                 if (treeWalk.getNameString().equals(part)) {
                                     if (treeWalk.isSubtree()) {
                                         treeWalk.enterSubtree();
                                         found = true;
                                         break;
                                     }
                                 }
                             }
                             if (!found) {
                                 return Collections.emptyList(); // Path not found
                             }
                        }
                    }

                    while (treeWalk.next()) {
                        files.add(new FileEntry(
                                treeWalk.getNameString(),
                                treeWalk.getPathString(),
                                treeWalk.isSubtree()
                        ));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public String getFileContent(String repoName, String refName, String path) {
        File repoDir = new File(rootDir, repoName);
        if (!repoDir.exists()) {
            return "Repository not found";
        }

        try (Git git = Git.open(repoDir); Repository repository = git.getRepository()) {
            ObjectId commitId = repository.resolve(refName != null ? refName : Constants.HEAD);
            if (commitId == null) {
                return "Commit not found";
            }

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                RevTree tree = commit.getTree();

                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    treeWalk.setFilter(PathFilter.create(path));

                    if (!treeWalk.next()) {
                        return "File not found";
                    }

                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader loader = repository.open(objectId);
                    return new String(loader.getBytes(), StandardCharsets.UTF_8);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading file: " + e.getMessage();
        }
    }

    public record FileEntry(String name, String path, boolean isDirectory) {}
}
