package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class TCListRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudConfigService configService;
    private final TurboCrudListColumnCallbackRegistry columnCallbackRegistry;
    private final FormCreator formCreator;
    private final TurboCrudDialogFactoryRegistry dialogFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry routeFactoryRegistry;

    public TCListRouteFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                  TurboCrudConfigService configService,
                                  TurboCrudListColumnCallbackRegistry columnCallbackRegistry,
                                  FormCreator formCreator,
                                  TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                                  TurboCrudRouteFactoryRegistry routeFactoryRegistry
    ) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.configService = configService;
        this.columnCallbackRegistry = columnCallbackRegistry;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.routeFactoryRegistry = routeFactoryRegistry;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new List(currentPathIndex,
                routeResolver,
                entityManagerFactoryRegistry,
                configService,
                columnCallbackRegistry,
                formCreator,
                dialogFactoryRegistry,
                routeFactoryRegistry);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
