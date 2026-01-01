package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridRoute;
import lombok.experimental.SuperBuilder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqGridRoute extends GridRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static GridRoute.GridRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return GridRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }
}