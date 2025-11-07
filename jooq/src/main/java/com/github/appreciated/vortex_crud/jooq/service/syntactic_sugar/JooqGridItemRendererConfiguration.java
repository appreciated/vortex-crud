package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridItemRendererConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqGridItemRendererConfiguration extends GridItemRendererConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static GridItemRendererConfiguration.GridItemRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return GridItemRendererConfiguration.builder();
    }
}
