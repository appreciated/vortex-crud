package com.github.appreciated.vortex_crud.example.jooq.repository;

import com.github.appreciated.vortex_crud.example.jooq.entity.User;
import com.github.appreciated.vortex_crud.jooq.repository.JooqUserRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.github.appreciated.vortex_crud.jooq.models.tables.Users.USERS;

@Repository
public class UserRepository extends JooqUserRepository<org.jooq.Record, User> {
    public UserRepository(DSLContext dsl) {
        super(dsl, USERS, USERS.ID, USERS.USERNAME, USERS.PASSWORD, User.class);
    }
}
