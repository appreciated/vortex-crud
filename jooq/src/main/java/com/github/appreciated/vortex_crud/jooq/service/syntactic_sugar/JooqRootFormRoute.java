package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.RootFormRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqRootFormRoute extends RootFormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static RootFormRoute.RootFormRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return RootFormRoute.rootFormRouteBuilder();
    }
}
