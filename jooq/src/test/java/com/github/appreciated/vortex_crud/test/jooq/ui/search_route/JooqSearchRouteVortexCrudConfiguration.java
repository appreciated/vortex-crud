package com.github.appreciated.vortex_crud.test.jooq.ui.search_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SearchRoute;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqApplication;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqDataStoreConfig;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqGridRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.SEARCH_ROUTE_TEST;

@Configuration
public class JooqSearchRouteVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Create DataStore config for the search route test entity
        @SuppressWarnings("unchecked")
        DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> config =
                (DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>) (DataStoreConfig<?, ?, ?>)
                JooqDataStoreConfig.builder(SEARCH_ROUTE_TEST, null).build();

        // Create the grid route that will be searchable
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> gridRoute = JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("Grid")
                .filterField(SEARCH_ROUTE_TEST.NAME)
                .build();

        // Create search route with explicit searchable routes
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> searchRoute = SearchRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .title("Search")
                .searchableRoutes(List.of(gridRoute))
                .build();

        return JooqApplication.builder()
                .applicationName("Search Test App")
                .routes(Map.of(
                        "grid", gridRoute,
                        "search", searchRoute
                ))
                .build();
    }
}
