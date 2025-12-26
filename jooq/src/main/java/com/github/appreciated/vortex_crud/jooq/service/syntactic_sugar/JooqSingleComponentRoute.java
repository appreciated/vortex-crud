package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.SingleComponentRoute;
import com.github.appreciated.vortex_crud.core.ui.factories.route.single_component.SingleComponentRouteFactory;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqSingleComponentRoute extends SingleComponentRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static SingleComponentRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return SingleComponentRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .factory(new SingleComponentRouteFactory<>());
    }
}
