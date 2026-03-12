package com.github.appreciated.vortex_crud.demo.devplatform.service.ssh;

import org.apache.sshd.git.pack.GitPackCommandFactory;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class CustomGitPackCommandFactory extends GitPackCommandFactory {

    private final GitAuthorizationService authorizationService;

    public CustomGitPackCommandFactory(GitAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
        withGitLocationResolver((cmd, args, session, fs) -> {
            String path = (args != null && args.length > 0) ? args[0] : "";
            String slug = cleanRepoPath(path);
            return Paths.get("repositories").resolve(slug);
        });
    }

    @Override
    public Command createCommand(ChannelSession channel, String command) throws IOException {
        String[] args = command.split("\\s+");
        if (args.length >= 2) {
            String action = args[0];
            String repoPath = args[1];
            String slug = cleanRepoPath(repoPath);

            Integer userId = channel.getSession().getAttribute(DatabasePublickeyAuthenticator.USER_ID_ATTRIBUTE);
            boolean allowed = false;

            if (userId != null) {
                if ("git-upload-pack".equals(action)) {
                    allowed = authorizationService.canRead(userId, slug);
                } else if ("git-receive-pack".equals(action)) {
                    allowed = authorizationService.canWrite(userId, slug);
                }
            }

            if (!allowed) {
                return new Command() {
                    @Override
                    public void setInputStream(java.io.InputStream in) {}
                    @Override
                    public void setOutputStream(java.io.OutputStream out) {}
                    @Override
                    public void setErrorStream(java.io.OutputStream err) {}
                    @Override
                    public void setExitCallback(org.apache.sshd.server.ExitCallback callback) {
                        callback.onExit(1, "Access denied to repository: " + slug);
                    }
                    @Override
                    public void start(ChannelSession channel, org.apache.sshd.server.Environment env) throws IOException {
                        // handled by exit callback
                    }
                    @Override
                    public void destroy(ChannelSession channel) {}
                };
            }
        }

        return super.createCommand(channel, command);
    }

    private String cleanRepoPath(String path) {
        if (path.startsWith("'") && path.endsWith("'")) {
            path = path.substring(1, path.length() - 1);
        } else if (path.startsWith("\"") && path.endsWith("\"")) {
            path = path.substring(1, path.length() - 1);
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith(".git")) {
            path = path.substring(0, path.length() - 4);
        }
        return path;
    }
}
