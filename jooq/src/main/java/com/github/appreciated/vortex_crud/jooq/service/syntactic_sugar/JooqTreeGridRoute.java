package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.TreeGridRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqTreeGridRoute extends TreeGridRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static TreeGridRoute.TreeGridRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return TreeGridRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }
}
