package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.VideoFieldRendererConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqVideoFieldRendererConfiguration {
    public static VideoFieldRendererConfiguration.VideoFieldRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return VideoFieldRendererConfiguration.builder();
    }
}