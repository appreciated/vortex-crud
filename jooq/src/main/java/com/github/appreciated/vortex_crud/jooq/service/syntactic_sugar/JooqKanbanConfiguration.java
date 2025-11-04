package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.KanbanConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqKanbanConfiguration {

    /**
     * Create a new JOOQ kanban configuration builder
     */
    public static KanbanConfiguration.KanbanConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return KanbanConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }

    /**
     * Create a new JOOQ kanban configuration builder
     */
    public static KanbanConfiguration.KanbanConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of() {
        return KanbanConfiguration.builder();
    }
}