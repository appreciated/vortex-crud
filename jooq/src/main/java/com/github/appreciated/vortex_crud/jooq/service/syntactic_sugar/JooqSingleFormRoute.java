package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.SingleFormRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqSingleFormRoute extends SingleFormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static SingleFormRoute.SingleFormRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return SingleFormRoute.builder();
    }
}
