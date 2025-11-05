package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.GridItemRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqGridItemRendererConfiguration {

    /**
     * Create a new JOOQ grid item renderer configuration builder with the specified factory
     */
    @SuppressWarnings("unchecked")
    public static GridItemRendererConfiguration.GridItemRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(Class<? extends VortexCrudItemFactory> factory) {
        return GridItemRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<TableField<?, ?>>>) factory);
    }

    /**
     * Create a new JOOQ grid item renderer configuration builder
     */
    @SuppressWarnings("unchecked")
    public static GridItemRendererConfiguration.GridItemRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return GridItemRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<TableField<?, ?>>>) (Class<?>) CardFactory.class);
    }
}
