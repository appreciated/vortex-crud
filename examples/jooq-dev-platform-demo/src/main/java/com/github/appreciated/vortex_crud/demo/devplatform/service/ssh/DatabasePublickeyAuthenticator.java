package com.github.appreciated.vortex_crud.demo.devplatform.service.ssh;

import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.SshKeys;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.Users;
import org.apache.sshd.common.AttributeRepository;
import org.apache.sshd.common.config.keys.AuthorizedKeyEntry;
import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.List;

@Component
public class DatabasePublickeyAuthenticator implements PublickeyAuthenticator {

    public static final AttributeRepository.AttributeKey<Integer> USER_ID_ATTRIBUTE = new AttributeRepository.AttributeKey<>();
    private final DSLContext dsl;

    public DatabasePublickeyAuthenticator(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) {
        // Find user by username
        Integer userId = dsl.select(Users.USERS.ID)
                .from(Users.USERS)
                .where(Users.USERS.USERNAME.eq(username))
                .fetchOneInto(Integer.class);

        if (userId == null) {
            return false;
        }

        // Fetch all public keys for the user
        List<String> storedKeys = dsl.select(SshKeys.SSH_KEYS.PUBLIC_KEY)
                .from(SshKeys.SSH_KEYS)
                .where(SshKeys.SSH_KEYS.USER_ID.eq(userId))
                .fetchInto(String.class);

        for (String storedKeyStr : storedKeys) {
            try {
                AuthorizedKeyEntry entry = AuthorizedKeyEntry.parseAuthorizedKeyEntry(storedKeyStr);
                if (entry == null) continue;

                PublicKey storedKey = entry.resolvePublicKey(session, null);

                if (KeyUtils.compareKeys(key, storedKey)) {
                    session.setAttribute(USER_ID_ATTRIBUTE, userId);
                    return true;
                }
            } catch (Exception e) {
                // Log error or ignore invalid keys
                e.printStackTrace();
            }
        }

        return false;
    }
}
