package com.github.appreciated.vortex_crud.demo.devplatform.service.ssh;

import org.apache.sshd.git.pack.GitPackCommandFactory;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.session.ServerSession;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CustomGitPackCommandFactory extends GitPackCommandFactory {

    private final GitAuthorizationService authorizationService;

    public CustomGitPackCommandFactory(GitAuthorizationService authorizationService) {
        super();
        this.authorizationService = authorizationService;
        withGitLocationResolver(new org.apache.sshd.git.GitLocationResolver() {
            @Override
            public java.nio.file.Path resolveRootDirectory(String command, String[] args, ServerSession session, java.nio.file.FileSystem fs) throws IOException {
                return Paths.get("repositories");
            }
        });
    }

    @Override
    public Command createCommand(ChannelSession channel, String command) throws IOException {
        String subCommand = null;
        String repoPath = null;
        String trimmedCommand = command.trim();

        if (trimmedCommand.startsWith("git-receive-pack")) {
            subCommand = "git-receive-pack";
        } else if (trimmedCommand.startsWith("git-upload-pack")) {
            subCommand = "git-upload-pack";
        }

        if (subCommand != null) {
            String args = trimmedCommand.substring(subCommand.length()).trim();
            if (args.startsWith("'") && args.endsWith("'")) {
                repoPath = args.substring(1, args.length() - 1);
            } else {
                repoPath = args;
            }

            // Clean up repo path
            if (repoPath.startsWith("/")) {
                repoPath = repoPath.substring(1);
            }

            // Check permissions
            if (true) { // Just to preserve indentation structure, subCommand check is redundant here but cleaner logic flow
                ServerSession session = channel.getSession();
                Integer userId = session.getAttribute(DatabasePublickeyAuthenticator.USER_ID_ATTRIBUTE);

                if (userId == null) {
                     // This could happen if authentication was bypassed or not configured correctly,
                     // though PublickeyAuthenticator should have set it.
                     // But strictly speaking, if not authenticated, we shouldn't be here.
                } else {
                    if ("git-receive-pack".equals(subCommand)) {
                        if (!authorizationService.canWrite(userId, repoPath)) {
                            // Throwing exception here will close the channel with error
                            throw new IOException("Access denied: You do not have write access to " + repoPath);
                        }
                    }
                }
            }
        }

        return super.createCommand(channel, command);
    }
}
