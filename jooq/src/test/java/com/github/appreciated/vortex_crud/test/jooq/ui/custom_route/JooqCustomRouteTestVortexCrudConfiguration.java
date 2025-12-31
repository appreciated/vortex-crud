package com.github.appreciated.vortex_crud.test.jooq.ui.custom_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqApplication;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JooqCustomRouteTestVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // According to memory: When configuring CustomRoute, explicit type witnessing with three generics
        // (e.g., .<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()) is required to avoid type inference errors.
        Map<String, RouteRenderer<?, ?, ?>> routes = Map.of(
                "dashboard", CustomRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .componentClass(CustomDashboardView.class)
                        .title("route.dashboard.title")
                        .iconFactory(VaadinIcon.DASHBOARD::create)
                        .defaultRoute(true)
                        .build()
        );

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
