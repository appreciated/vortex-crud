package com.github.appreciated.vortex_crud.demo.projectmanagement.ui;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskRecord;
import com.github.appreciated.vortex_crud.demo.projectmanagement.ui.view.DashboardView;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.function.SerializableSupplier;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.List;

public class DashboardRouteRenderer implements RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final JooqDataStore<TaskRecord> taskStore;

    public DashboardRouteRenderer(JooqDataStore<TaskRecord> taskStore) {
        this.taskStore = taskStore;
    }

    @Override
    public DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> dataStoreConfig() {
        return null;
    }

    @Override
    public String title() {
        return "route.dashboard.title";
    }

    @Override
    public boolean defaultRoute() {
        return true;
    }

    @Override
    public VortexCrudRouteFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> factory() {
        return new VortexCrudRouteFactory<>() {
            @Override
            public Component renderRoute(VortexCrudContext<TableRecord<?>, TableField<?, ?>, TableImpl<?>> context, Integer currentPathIndex, VortexCrudPathToRouteResolver routeResolver, DetailRouteSetting detailRouteSetting) {
                return new DashboardView(taskStore);
            }

            @Override
            public boolean isContainerRoute() {
                return false;
            }
        };
    }

    @Override
    public boolean hiddenInMenu() {
        return false;
    }

    @Override
    public SerializableSupplier<Component> iconFactory() {
        return VaadinIcon.DASHBOARD::create;
    }

    @Override
    public List<String> writeRoles() {
        return List.of();
    }

    @Override
    public List<String> readOnlyRoles() {
        return List.of("viewer", "member", "admin", "manager");
    }
}
