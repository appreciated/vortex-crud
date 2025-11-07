package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormRendererConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqFormRendererConfiguration extends FormRendererConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static FormRendererConfiguration.FormRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return FormRendererConfiguration.builder();
    }
}
