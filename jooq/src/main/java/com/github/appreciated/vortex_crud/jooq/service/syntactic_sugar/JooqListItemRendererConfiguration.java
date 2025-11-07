package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ListItemRendererConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqListItemRendererConfiguration extends ListItemRendererConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static ListItemRendererConfiguration.ListItemRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return ListItemRendererConfiguration.builder();
    }
}