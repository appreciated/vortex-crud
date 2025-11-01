package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

/**
 * JOOQ-specific grid route builder.
 * Usage: JooqGridRoute.builder()
 *     .withDataStore(TABLE)
 *     .withTitleField(TABLE.FIELD)
 *     .withFilterField(TABLE.FIELD)
 *     .withChildren(...)
 *     .build()
 */
public class JooqGridRoute {

    /**
     * Create a new JOOQ grid route builder
     */
    public static GridRoute.GridBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of() {
        return GridRoute.builder();
    }
}