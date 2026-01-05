package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import lombok.experimental.SuperBuilder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqFormRoute extends FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static FormRoute.FormRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return FormRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder();
    }
}