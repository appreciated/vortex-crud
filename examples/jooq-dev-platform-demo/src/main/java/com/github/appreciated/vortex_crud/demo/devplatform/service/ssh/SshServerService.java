package com.github.appreciated.vortex_crud.demo.devplatform.service.ssh;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class SshServerService {

    private static final Logger log = LoggerFactory.getLogger(SshServerService.class);

    private SshServer sshd;
    private final DatabasePublickeyAuthenticator authenticator;
    private final CustomGitPackCommandFactory commandFactory;

    public SshServerService(DatabasePublickeyAuthenticator authenticator,
                            CustomGitPackCommandFactory commandFactory) {
        this.authenticator = authenticator;
        this.commandFactory = commandFactory;
    }

    @PostConstruct
    public void start() throws IOException {
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(2222);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));
        sshd.setPublickeyAuthenticator(authenticator);
        sshd.setCommandFactory(commandFactory);

        sshd.start();
        log.info("SSH Server started on port 2222");
    }

    @PreDestroy
    public void stop() {
        if (sshd != null) {
            try {
                sshd.stop();
                log.info("SSH Server stopped");
            } catch (IOException e) {
                log.error("Failed to stop SSH Server", e);
            }
        }
    }
}
