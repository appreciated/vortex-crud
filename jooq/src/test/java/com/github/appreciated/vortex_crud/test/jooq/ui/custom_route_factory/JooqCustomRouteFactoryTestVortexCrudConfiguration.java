package com.github.appreciated.vortex_crud.test.jooq.ui.custom_route_factory;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.config.model.SubmenuRoute;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class JooqCustomRouteFactoryTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        return Application.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .applicationName("JOOQ Custom Route Factory Test")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(new LinkedHashMap<>(Map.of(
                        "submenu", SubmenuRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .title("Submenu")
                                .childrenMap(new LinkedHashMap<>(Map.of(
                                        "custom-nested", CustomRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                                .title("Nested Custom")
                                                // No component class provided, or even if provided, nested route handled by InternalDynamicRoute uses factory
                                                .build()
                                )))
                                .build()
                )))
                .build();
    }
}
