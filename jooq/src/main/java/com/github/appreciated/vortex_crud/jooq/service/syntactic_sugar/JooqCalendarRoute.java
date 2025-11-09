package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CalendarRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqCalendarRoute extends CalendarRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static CalendarRoute.CalendarRouteBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return CalendarRoute.builder();
    }
}
