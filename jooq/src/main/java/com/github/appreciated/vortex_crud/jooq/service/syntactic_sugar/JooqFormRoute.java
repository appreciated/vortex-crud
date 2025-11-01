package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

/**
 * JOOQ-specific form route builder.
 * Usage: JooqFormRoute.builder()
 *     .withDataStore(TABLE)
 *     .withTitleField(TABLE.FIELD)
 *     .withChildren(...)
 *     .build()
 */
public class JooqFormRoute {

    /**
     * Create a new JOOQ form route builder
     */
    public static FormRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of() {
        return FormRoute.builder();
    }
}