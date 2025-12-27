package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormSlideRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqFormSlideRoute extends FormSlideRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static FormSlideRoute.FormSlideRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return FormSlideRoute.builder();
    }
}
