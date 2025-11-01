package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.KanbanRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

/**
 * JOOQ-specific kanban route builder.
 * Usage: JooqKanbanRoute.builder()
 *     .withDataStore(TABLE)
 *     .withColumnField(TABLE.STATUS)  // Required
 *     .withTitleField(TABLE.TITLE)
 *     .withChildren(...)
 *     .build()
 */
public class JooqKanbanRoute {

    /**
     * Create a new JOOQ kanban route builder
     */
    public static KanbanRoute.KanbanBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of() {
        return KanbanRoute.builder();
    }
}
