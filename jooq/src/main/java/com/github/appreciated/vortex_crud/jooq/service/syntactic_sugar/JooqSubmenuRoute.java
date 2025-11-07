package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.SubmenuRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqSubmenuRoute extends SubmenuRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static SubmenuRoute.SubmenuRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return SubmenuRoute.builder();
    }
}
