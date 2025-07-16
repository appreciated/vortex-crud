package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.jooq.impl.TableImpl;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqApplication extends Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public static Application.Builder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of() {
        return new Application.Builder<>(new JooqApplication());
    }
}
