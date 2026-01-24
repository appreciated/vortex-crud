package com.github.appreciated.vortex_crud.demo.devplatform;

import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.Repository;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.SshKeys;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.Users;
import com.github.appreciated.vortex_crud.demo.devplatform.service.GitService;
import org.apache.sshd.common.config.keys.PublicKeyEntry;
import org.apache.sshd.common.keyprovider.KeyIdentityProvider;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import com.github.appreciated.vortex_crud.demo.devplatform.service.ssh.SshServerService;

@SpringBootTest(properties = "app.ssh.port=0")
public class SshGitServerTest {

    @Autowired
    private GitService gitService;

    @Autowired
    private SshServerService sshServerService;

    @Autowired
    private DSLContext dsl;

    @Test
    public void testSshGitAuthentication() throws Exception {
        // 1. Create User and SSH Key
        String username = "testuser_" + System.currentTimeMillis();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        StringBuilder sb = new StringBuilder();
        PublicKeyEntry.appendPublicKeyEntry(sb, keyPair.getPublic());
        String publicKeyStr = sb.toString();

        dsl.insertInto(Users.USERS)
            .set(Users.USERS.USERNAME, username)
            .set(Users.USERS.PASSWORD_HASH, "hash")
            .execute();

        Integer userId = dsl.select(Users.USERS.ID).from(Users.USERS).where(Users.USERS.USERNAME.eq(username)).fetchOneInto(Integer.class);

        dsl.insertInto(SshKeys.SSH_KEYS)
            .set(SshKeys.SSH_KEYS.USER_ID, userId)
            .set(SshKeys.SSH_KEYS.PUBLIC_KEY, publicKeyStr)
            .execute();

        // 2. Create Repo (in DB and FS)
        String repoSlug = "test-ssh-repo-" + System.currentTimeMillis();
        dsl.insertInto(Repository.REPOSITORY)
            .set(Repository.REPOSITORY.NAME, "Test SSH Repo")
            .set(Repository.REPOSITORY.SLUG, repoSlug)
            .set(Repository.REPOSITORY.OWNER_ID, userId)
            .execute();

        gitService.initRepository(repoSlug); // Creates bare repo

        // 3. Connect via SSH
        try (org.apache.sshd.client.SshClient client = org.apache.sshd.client.SshClient.setUpDefaultClient()) {
            client.start();
            client.setKeyIdentityProvider(KeyIdentityProvider.wrapKeyPairs(keyPair));

            try (org.apache.sshd.client.session.ClientSession session = client.connect(username, "localhost", sshServerService.getPort()).verify(5000).getSession()) {
                session.auth().verify(5000);

                if (!session.isAuthenticated()) {
                    throw new RuntimeException("Authentication failed");
                }

                System.out.println("Authentication successful for " + username);
            }
        }
    }
}
