package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.Application;
import org.jooq.Table;

public class JooqApplication extends Application<Table<?>> {

    public static Application.Builder<Table<?>> of() {
        return new Application.Builder<>(new Application<Table<?>>());
    }
}
