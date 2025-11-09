package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CalendarRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaCalendarRoute extends CalendarRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {
    public static CalendarRoute.CalendarRouteBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder() {
        return CalendarRoute.builder();
    }
}
