package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqRouteRenderer {
    public static ListRoute.ListRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(Class<? extends VortexCrudRouteFactory> factory) {
        return ListRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .factory((Class<? extends VortexCrudRouteFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) factory);
    }
}