package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.MasterDetailRoute;
import lombok.experimental.SuperBuilder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqMasterDetailRoute extends MasterDetailRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static MasterDetailRoute.MasterDetailRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return MasterDetailRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }
}
