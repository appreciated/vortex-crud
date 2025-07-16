package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqApplication extends Application<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> {

    public static Application.Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of() {
        return new Application.Builder<>(new JooqApplication());
    }
}
