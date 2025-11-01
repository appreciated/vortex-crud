package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

/**
 * JOOQ-specific list route builder.
 * Usage: JooqListRoute.of()
 *     .withDataStore(TABLE)
 *     .withTitleField(TABLE.FIELD)
 *     .withFilterField(TABLE.FIELD)
 *     .withChildren(...)
 *     .build()
 */
public class JooqListRoute {

    /**
     * Create a new JOOQ list route builder
     */
    public static ListRoute.ListBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of() {
        return ListRoute.of();
    }
}
