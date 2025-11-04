package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

/**
 * JOOQ-specific list route builder.
 * Usage: JooqListRoute.builder()
 *     .dataStore(TABLE)
 *     .titleField(TABLE.FIELD)
 *     .filterField(TABLE.FIELD)
 *     .children(...)
 *     .build()
 */
public class JooqListRoute {
    public static ListRoute.ListRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return ListRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }
}
