package com.github.appreciated.vortex_crud.jooq.repository;

import com.github.appreciated.vortex_crud.auth.repository.VortexCrudUserRepository;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudUser;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.stream.Collectors;

public abstract class JooqUserRepository<R extends org.jooq.Record, T extends VortexCrudUser> implements VortexCrudUserRepository {

    private final DSLContext dsl;
    private final Table<R> table;
    private final TableField<R, Long> idField;
    private final TableField<R, String> usernameField;
    private final TableField<R, String> passwordField;
    private final Class<T> userClass;

    public JooqUserRepository(DSLContext dsl, Table<R> table, TableField<R, Long> idField, TableField<R, String> usernameField, TableField<R, String> passwordField, Class<T> userClass) {
        this.dsl = dsl;
        this.table = table;
        this.idField = idField;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.userClass = userClass;
    }

    @Override
    public Optional<VortexCrudUser> findByUsername(String username) {
        return dsl.selectFrom(table)
                .where(usernameField.eq(username))
                .fetchOptionalInto(userClass)
                .map(user -> (VortexCrudUser) user);
    }

    @Override
    public VortexCrudUser save(VortexCrudUser user) {
        T jooqUser = (T) user;
        // This is a simplified save method. A real implementation would need to handle roles and other fields.
        if (dsl.fetchExists(dsl.selectFrom(table).where(usernameField.eq(jooqUser.getUsername())))) {
            dsl.update(table)
                    .set(passwordField, jooqUser.getPassword())
                    .where(usernameField.eq(jooqUser.getUsername()))
                    .execute();
        } else {
            dsl.insertInto(table)
                    .set(usernameField, jooqUser.getUsername())
                    .set(passwordField, jooqUser.getPassword())
                    .execute();
        }
        return jooqUser;
    }

    @Override
    public boolean existsByUsername(String username) {
        return dsl.fetchExists(dsl.selectFrom(table).where(usernameField.eq(username)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(role -> (org.springframework.security.core.GrantedAuthority) () -> "ROLE_" + role)
                                .collect(Collectors.toList())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
