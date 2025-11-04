package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.MasterDetailRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqMasterDetailRoute {
    public static MasterDetailRoute.MasterDetailRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return MasterDetailRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }
}
