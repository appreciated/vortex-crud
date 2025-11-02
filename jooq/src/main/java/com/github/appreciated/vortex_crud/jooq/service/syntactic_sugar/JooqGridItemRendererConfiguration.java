package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.GridItemRendererConfiguration;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqGridItemRendererConfiguration {

    public static FormRoute.FormRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of() {
        return FormRoute.builder();
    }

    public static GridItemRendererConfiguration.GridItemRendererConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>, ?, ?> of(Class<? extends VortexCrudItemFactory> factory) {
        return GridItemRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .factory((Class<? extends VortexCrudItemFactory<TableField<?, ?>>>) factory);
    }
}