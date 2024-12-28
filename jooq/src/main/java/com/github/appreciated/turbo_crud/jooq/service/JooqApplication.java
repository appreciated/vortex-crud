package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.Application;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqApplication extends Application<Table<?>, TableField<?,?>> {

    public static Application.Builder<Table<?>, TableField<?,?>> of() {
        return new Application.Builder<>(new Application<Table<?>,TableField<?,?>>());
    }
}
