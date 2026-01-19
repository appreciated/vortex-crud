package com.github.appreciated.vortex_crud.demo.devplatform;

import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class GitServletConfiguration {

    @Bean
    public ServletRegistrationBean<GitServlet> gitServlet() {
        GitServlet gitServlet = new GitServlet();
        gitServlet.setRepositoryResolver(new LocalRepositoryResolver());

        ServletRegistrationBean<GitServlet> bean = new ServletRegistrationBean<>(gitServlet, "/git/*");
        bean.setName("GitServlet");
        bean.addInitParameter("base-path", "repositories");
        bean.addInitParameter("export-all", "true");
        return bean;
    }

    private static class LocalRepositoryResolver implements RepositoryResolver<HttpServletRequest> {
        private static final File REPO_ROOT = new File("repositories");

        @Override
        public Repository open(HttpServletRequest req, String name) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
            if (name.startsWith("/git/")) {
                name = name.substring(5);
            }
            if (name.startsWith("/")) {
                name = name.substring(1);
            }
            // Strip trailing .git if present
            if (name.endsWith(".git")) {
                name = name.substring(0, name.length() - 4);
            }

            // Security check
            if (name.contains("..") || name.contains("\\")) {
                 throw new RepositoryNotFoundException(name);
            }

            File gitDir = new File(REPO_ROOT, name + "/.git");
            if (!gitDir.exists()) {
                 gitDir = new File(REPO_ROOT, name + "/.git");
            }

            if (!gitDir.exists()) {
                throw new RepositoryNotFoundException(name);
            }

            try {
                return new FileRepositoryBuilder()
                        .setGitDir(gitDir)
                        .build();
            } catch (IOException e) {
                throw new ServiceMayNotContinueException(e);
            }
        }
    }

    private static class RepositoryNotFoundException extends org.eclipse.jgit.errors.RepositoryNotFoundException {
        public RepositoryNotFoundException(String name) {
            super(name);
        }
    }
}
