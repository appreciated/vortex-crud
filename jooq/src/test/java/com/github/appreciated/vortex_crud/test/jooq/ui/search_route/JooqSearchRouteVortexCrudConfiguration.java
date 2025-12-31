package com.github.appreciated.vortex_crud.test.jooq.ui.search_route;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SearchRoute;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.models.Tables;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.SearchRouteTestRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqApplication;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqDataStoreConfig;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqGridRoute;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextField;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.SEARCH_ROUTE_TEST;

@Configuration
public class JooqSearchRouteVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqSearchRouteVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Create DataStore for the search route test entity
        JooqDataStore<SearchRouteTestRecord> store = new JooqDataStore<>(SEARCH_ROUTE_TEST.getRecordType(), dsl);

        @SuppressWarnings("unchecked")
        DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> config =
                JooqDataStoreConfig.of(SEARCH_ROUTE_TEST)
                        .dataStoreInstance(store)
                        .fields(Map.of(
                                SEARCH_ROUTE_TEST.ID, JooqNumericIdField.builder().build(),
                                SEARCH_ROUTE_TEST.NAME, JooqTextField.builder().build()
                        ))
                        .build();

        // Create the grid route that will be searchable
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> gridRoute = JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("Grid")
                .filterField(SEARCH_ROUTE_TEST.NAME)
                .build();

        // Create search route with explicit searchable routes
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> searchRoute = SearchRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .title("Search")
                .defaultRoute(true)
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
