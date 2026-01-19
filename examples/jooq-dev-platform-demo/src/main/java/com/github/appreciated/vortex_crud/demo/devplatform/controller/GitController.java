package com.github.appreciated.vortex_crud.demo.devplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

@Controller
@RequestMapping("/git")
public class GitController {

    private static final File REPO_ROOT = new File("repositories");

    @GetMapping("/{slug}/info/refs")
    public void infoRefs(@PathVariable String slug,
                         @RequestParam("service") String service,
                         HttpServletResponse response) throws IOException {

        File gitDir = getGitDir(slug);
        if (gitDir == null) {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return;
        }

        if (!"git-upload-pack".equals(service) && !"git-receive-pack".equals(service)) {
            response.sendError(HttpStatus.FORBIDDEN.value());
            return;
        }

        response.setContentType("application/x-" + service + "-advertisement");
        response.setHeader("Cache-Control", "no-cache");

        try (Repository repository = new FileRepositoryBuilder().setGitDir(gitDir).build()) {
            PacketLineOut pckOut = new PacketLineOut(response.getOutputStream());
            pckOut.writeString("# service=" + service + "\n");
            pckOut.end();

            if ("git-upload-pack".equals(service)) {
                UploadPack uploadPack = new UploadPack(repository);
                uploadPack.setBiDirectionalPipe(false);
                uploadPack.sendAdvertisedRefs(new RefAdvertiser.PacketLineOutRefAdvertiser(pckOut));
            } else {
                ReceivePack receivePack = new ReceivePack(repository);
                receivePack.sendAdvertisedRefs(new RefAdvertiser.PacketLineOutRefAdvertiser(pckOut));
            }
        }
    }

    @PostMapping("/{slug}/git-upload-pack")
    public void uploadPack(@PathVariable String slug,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        File gitDir = getGitDir(slug);
        if (gitDir == null) {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return;
        }

        response.setContentType("application/x-git-upload-pack-result");
        response.setHeader("Cache-Control", "no-cache");

        try (Repository repository = new FileRepositoryBuilder().setGitDir(gitDir).build()) {
            UploadPack uploadPack = new UploadPack(repository);
            uploadPack.setBiDirectionalPipe(false);

            InputStream input = request.getInputStream();
            if ("gzip".equals(request.getHeader("Content-Encoding"))) {
                input = new GZIPInputStream(input);
            }

            uploadPack.upload(input, response.getOutputStream(), null);
        }
    }

    @PostMapping("/{slug}/git-receive-pack")
    public void receivePack(@PathVariable String slug,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        File gitDir = getGitDir(slug);
        if (gitDir == null) {
            response.sendError(HttpStatus.NOT_FOUND.value());
            return;
        }

        response.setContentType("application/x-git-receive-pack-result");
        response.setHeader("Cache-Control", "no-cache");

        try (Repository repository = new FileRepositoryBuilder().setGitDir(gitDir).build()) {
            ReceivePack receivePack = new ReceivePack(repository);
            receivePack.setBiDirectionalPipe(false);

            InputStream input = request.getInputStream();
             if ("gzip".equals(request.getHeader("Content-Encoding"))) {
                input = new GZIPInputStream(input);
            }

            receivePack.receive(input, response.getOutputStream(), null);
        }
    }

    private File getGitDir(String slug) {
        if (slug.endsWith(".git")) {
            slug = slug.substring(0, slug.length() - 4);
        }
        File repoDir = new File(REPO_ROOT, slug);
        File gitDir = new File(repoDir, ".git");
        if (gitDir.exists()) {
            return gitDir;
        }
        return null;
    }
}
