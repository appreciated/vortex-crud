package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqApplication extends Application<Table<?>, TableField<?,?>> {

    public static Application.Builder<Table<?>, TableField<?,?>> of() {
        return new Application.Builder<>(new JooqApplication());
    }
}
