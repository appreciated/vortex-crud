package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqApplication extends Application<Class<? extends TableRecord<?>>, TableField<?,?>> {

    public static Application.Builder<Class<? extends TableRecord<?>>, TableField<?,?>> of() {
        return new Application.Builder<>(new JooqApplication());
    }
}
