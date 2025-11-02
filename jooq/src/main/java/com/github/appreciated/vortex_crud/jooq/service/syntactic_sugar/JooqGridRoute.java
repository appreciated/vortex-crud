package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

/**
 * JOOQ-specific grid route builder.
 * Usage: JooqGridRoute.builder()
 *     .dataStore(TABLE)
 *     .titleField(TABLE.FIELD)
 *     .filterField(TABLE.FIELD)
 *     .children(...)
 *     .build()
 */
public class JooqGridRoute {

    /**
     * Create a new JOOQ grid route builder
     */
    public static GridRoute.GridRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of() {
        return GridRoute.builder();
    }
}