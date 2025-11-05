package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqImageFieldRendererConfiguration {
    public static ImageFieldRendererConfiguration.ImageFieldRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return ImageFieldRendererConfiguration.builder();
    }
}