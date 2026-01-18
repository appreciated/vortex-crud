package com.github.appreciated.vortex_crud.demo.projectmanagement.ui;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.function.Supplier;

public class SupplierRouteFactory implements VortexCrudRouteFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final Supplier<Component> supplier;

    public SupplierRouteFactory(Supplier<Component> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Component renderRoute(
            VortexCrudContext<TableRecord<?>, TableField<?, ?>, TableImpl<?>> context,
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver routeResolver,
            DetailRouteSetting detailRouteSetting
    ) {
        return supplier.get();
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
