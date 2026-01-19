package com.github.appreciated.vortex_crud.demo.devplatform.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> listBranches(String slug) {
        File repoDir = new File(REPO_ROOT, slug);
        if (!repoDir.exists()) return Collections.emptyList();

        try (Git git = Git.open(repoDir)) {
            return git.branchList()
                    .setListMode(ListBranchCommand.ListMode.ALL)
                    .call()
                    .stream()
                    .map(Ref::getName)
                    .map(Repository::shortenRefName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void createBranch(String slug, String branchName, String startPoint) {
        File repoDir = new File(REPO_ROOT, slug);
        try (Git git = Git.open(repoDir)) {
            git.branchCreate()
                    .setName(branchName)
                    .setStartPoint(startPoint)
                    .call();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create branch", e);
        }
    }

    public void deleteBranch(String slug, String branchName) {
        File repoDir = new File(REPO_ROOT, slug);
        try (Git git = Git.open(repoDir)) {
            git.branchDelete()
                    .setBranchNames(branchName)
                    .setForce(true)
                    .call();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete branch", e);
        }
    }

    public List<FileEntry> listFiles(String slug, String path) {
        return listFiles(slug, path, "HEAD");
    }

    public List<FileEntry> listFiles(String slug, String path, String ref) {
        File repoDir = new File(REPO_ROOT, slug);
        List<FileEntry> files = new ArrayList<>();

        if (!repoDir.exists()) {
             return files;
        }

        try (Git git = Git.open(repoDir)) {
            Repository repository = git.getRepository();
            ObjectId commitId = repository.resolve(ref);
            if (commitId == null) return files;

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(commitId);
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
        return getFileContent(slug, path, "HEAD");
    }

    public String getFileContent(String slug, String path, String ref) {
         File repoDir = new File(REPO_ROOT, slug);
         if (!repoDir.exists()) return null;

         try (Git git = Git.open(repoDir)) {
            Repository repository = git.getRepository();
            ObjectId commitId = repository.resolve(ref);
            if (commitId == null) return null;

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(commitId);
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

    public byte[] getRawFileContent(String slug, String path, String ref) {
         File repoDir = new File(REPO_ROOT, slug);
         if (!repoDir.exists()) return null;

         try (Git git = Git.open(repoDir)) {
            Repository repository = git.getRepository();
            ObjectId commitId = repository.resolve(ref);
            if (commitId == null) return null;

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                RevTree tree = commit.getTree();

                try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree)) {
                    if (treeWalk == null) return null;

                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader loader = repository.open(objectId);

                    return loader.getBytes();
                }
            }
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
    }

    public List<CommitInfo> getCommitHistory(String slug, String ref) {
        File repoDir = new File(REPO_ROOT, slug);
        List<CommitInfo> commits = new ArrayList<>();
        if (!repoDir.exists()) return commits;

        try (Git git = Git.open(repoDir)) {
            Repository repository = git.getRepository();
            ObjectId commitId = repository.resolve(ref);
            if (commitId == null) return commits;

            Iterable<RevCommit> log = git.log().add(commitId).call();
            for (RevCommit commit : log) {
                commits.add(new CommitInfo(
                    commit.getName(),
                    commit.getShortMessage(),
                    commit.getFullMessage(),
                    commit.getAuthorIdent().getName(),
                    LocalDateTime.ofInstant(commit.getAuthorIdent().getWhen().toInstant(), ZoneId.systemDefault())
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commits;
    }

    public String getDiff(String slug, String oldRef, String newRef) {
        File repoDir = new File(REPO_ROOT, slug);
        if (!repoDir.exists()) return "";

        try (Git git = Git.open(repoDir)) {
            Repository repository = git.getRepository();

            // If oldRef is null (e.g., initial commit), we diff against empty tree
            AbstractTreeIterator oldTreeParser = null;
            if (oldRef != null) {
                ObjectId oldId = repository.resolve(oldRef);
                try (RevWalk walk = new RevWalk(repository)) {
                     RevCommit commit = walk.parseCommit(oldId);
                     if (commit.getParentCount() > 0) {
                        RevCommit parent = walk.parseCommit(commit.getParent(0).getId());
                        oldTreeParser = prepareTreeParser(repository, parent.getId());
                     } else {
                        // Initial commit, compare against empty tree
                         oldTreeParser = new CanonicalTreeParser();
                     }
                }
            } else {
                 oldTreeParser = new CanonicalTreeParser();
            }

            // However, often we want diff between commit and its parent.
            // If oldRef is passed explicitly, use it. If we are just viewing a single commit (newRef),
            // we want diff against its parent.

            // Let's assume this method is flexible.
            // If oldRef is provided, use it.
            // If oldRef is null, and newRef is provided, try to find parent of newRef.

            ObjectId newId = repository.resolve(newRef);
            AbstractTreeIterator newTreeParser = prepareTreeParser(repository, newId);

            if (oldTreeParser == null && oldRef == null) {
                 try (RevWalk walk = new RevWalk(repository)) {
                     RevCommit commit = walk.parseCommit(newId);
                     if (commit.getParentCount() > 0) {
                         oldTreeParser = prepareTreeParser(repository, commit.getParent(0).getId());
                     } else {
                         oldTreeParser = new CanonicalTreeParser();
                     }
                 }
            } else if (oldRef != null) {
                oldTreeParser = prepareTreeParser(repository, repository.resolve(oldRef));
            }


            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 DiffFormatter formatter = new DiffFormatter(out)) {
                formatter.setRepository(repository);
                formatter.setDiffComparator(RawTextComparator.DEFAULT);
                formatter.setDetectRenames(true);

                formatter.format(oldTreeParser, newTreeParser);
                return out.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error calculating diff: " + e.getMessage();
        }
    }

    private AbstractTreeIterator prepareTreeParser(Repository repository, ObjectId objectId) throws Exception {
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(objectId);
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            return treeParser;
        }
    }

    public List<BlameInfo> blame(String slug, String ref, String path) {
        File repoDir = new File(REPO_ROOT, slug);
        List<BlameInfo> blameInfos = new ArrayList<>();
        if (!repoDir.exists()) return blameInfos;

        try (Git git = Git.open(repoDir)) {
            Repository repository = git.getRepository();
            ObjectId commitId = repository.resolve(ref);

            BlameResult result = git.blame()
                    .setFilePath(path)
                    .setStartCommit(commitId)
                    .call();

            if (result == null) return blameInfos;

            RawTextComparator comparator = RawTextComparator.DEFAULT;
            int lines = result.getResultContents().size();
            for (int i = 0; i < lines; i++) {
                RevCommit commit = result.getSourceCommit(i);
                blameInfos.add(new BlameInfo(
                    i + 1,
                    commit.getName(),
                    commit.getAuthorIdent().getName(),
                    commit.getShortMessage(),
                    LocalDateTime.ofInstant(commit.getAuthorIdent().getWhen().toInstant(), ZoneId.systemDefault())
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return blameInfos;
    }

    public record FileEntry(String name, String path, boolean isDirectory) {}
    public record CommitInfo(String id, String shortMessage, String fullMessage, String author, LocalDateTime date) {}
    public record BlameInfo(int lineNumber, String commitId, String author, String message, LocalDateTime date) {}
}
