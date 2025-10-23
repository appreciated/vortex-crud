package com.github.appreciated.vortex_crud.jooq.repository;

import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public class Tables {

    public static final org.jooq.Table<?> USERS = DSL.table("users");
    public static final org.jooq.Table<?> USER_ROLES = DSL.table("user_roles");

    public static final org.jooq.Field<Long> USERS_ID = DSL.field("id", SQLDataType.BIGINT);
    public static final org.jooq.Field<String> USERS_USERNAME = DSL.field("username", SQLDataType.VARCHAR);
    public static final org.jooq.Field<String> USERS_PASSWORD = DSL.field("password", SQLDataType.VARCHAR);
    public static final org.jooq.Field<Long> USER_ROLES_USER_ID = DSL.field("user_id", SQLDataType.BIGINT);
    public static final org.jooq.Field<String> USER_ROLES_ROLE = DSL.field("role", SQLDataType.VARCHAR);

}
