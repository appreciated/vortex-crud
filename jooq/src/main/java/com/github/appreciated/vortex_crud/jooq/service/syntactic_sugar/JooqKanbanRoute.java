package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.KanbanRoute;
import lombok.experimental.SuperBuilder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqKanbanRoute extends KanbanRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static KanbanRoute.KanbanRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return KanbanRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }
}
