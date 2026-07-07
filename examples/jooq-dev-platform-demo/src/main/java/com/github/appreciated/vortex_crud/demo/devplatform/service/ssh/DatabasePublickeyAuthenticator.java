package com.github.appreciated.vortex_crud.demo.devplatform.service.ssh;

import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.SshKeysRecord;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.UsersRecord;
import org.apache.sshd.common.AttributeRepository.AttributeKey;
import org.apache.sshd.common.config.keys.AuthorizedKeyEntry;
import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.List;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.SSH_KEYS;
import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.USERS;

@Component
public class DatabasePublickeyAuthenticator implements PublickeyAuthenticator {

    private static final Logger log = LoggerFactory.getLogger(DatabasePublickeyAuthenticator.class);
    public static final AttributeKey<Integer> USER_ID_KEY = new AttributeKey<>();

    private final DSLContext dsl;

    public DatabasePublickeyAuthenticator(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) {
        try {
            UsersRecord user = dsl.selectFrom(USERS)
                .where(USERS.USERNAME.eq(username))
                .fetchOne();

            if (user == null) {
                log.debug("User not found: {}", username);
                return false;
            }

            List<SshKeysRecord> userKeys = dsl.selectFrom(SSH_KEYS)
                .where(SSH_KEYS.USER_ID.eq(user.getId()))
                .fetch();

            for (SshKeysRecord record : userKeys) {
                try {
                    String publicKeyStr = record.getPublicKey();
                    if (publicKeyStr == null || publicKeyStr.isBlank()) {
                        continue;
                    }

                    AuthorizedKeyEntry entry = AuthorizedKeyEntry.parseAuthorizedKeyEntry(publicKeyStr);
                    if (entry == null) {
                        continue;
                    }

                    PublicKey storedKey = entry.resolvePublicKey(session, null);
                    if (KeyUtils.compareKeys(storedKey, key)) {
                        session.setAttribute(USER_ID_KEY, user.getId());
                        return true;
                    }
                } catch (Exception e) {
                    log.warn("Failed to parse SSH key for user {}: {}", username, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error during authentication for user {}", username, e);
        }

        return false;
    }
}
