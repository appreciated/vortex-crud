package com.github.appreciated.vortex_crud.demo.devplatform.service.ssh;

import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class SshServerService {

    private final DatabasePublickeyAuthenticator authenticator;
    private final CustomGitPackCommandFactory commandFactory;

    @Value("${app.ssh.port:2222}")
    private int port;

    private SshServer sshd;

    public SshServerService(DatabasePublickeyAuthenticator authenticator, CustomGitPackCommandFactory commandFactory) {
        this.authenticator = authenticator;
        this.commandFactory = commandFactory;
    }

    @PostConstruct
    public void start() throws IOException {
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("host.ser")));
        sshd.setPublickeyAuthenticator(authenticator);
        sshd.setCommandFactory(commandFactory);
        sshd.start();
        System.out.println("SSH Server started on port " + sshd.getPort());
    }

    @PreDestroy
    public void stop() throws IOException {
        if (sshd != null) {
            sshd.stop();
        }
    }

    public int getPort() {
        return sshd != null ? sshd.getPort() : -1;
    }
}
