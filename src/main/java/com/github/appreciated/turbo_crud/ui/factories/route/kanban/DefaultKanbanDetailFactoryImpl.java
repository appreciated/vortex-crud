package com.github.appreciated.turbo_crud.ui.factories.route.kanban;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.KanbanConfig;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.component.KanbanView;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class DefaultKanbanDetailFactoryImpl implements TurboCrudRouteFactory {
    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudConfigService configService;
    private final TurboCrudItemFactoryRegistry turboCrudItemFactory;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final FormCreator formCreator;
    private final TurboCrudDialogFactoryRegistry dialogFactoryRegistry;
    private final TurboCrudIconFactory iconFactory;

    public DefaultKanbanDetailFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                          TurboCrudConfigService configService,
                                          TurboCrudItemFactoryRegistry turboCrudItemFactory,
                                          TurboCrudRouteFactoryRegistry routeFactory,
                                          FormCreator formCreator,
                                          TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                                          TurboCrudIconFactory iconFactory) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.configService = configService;
        this.turboCrudItemFactory = turboCrudItemFactory;
        this.routeFactory = routeFactory;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        Route route = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView(route.getRepository(),
                route,
                entityManagerFactoryRegistry.getFactory(route.getRepository()),
                routeFactory,
                turboCrudItemFactory,
                ConfigBeanFactory.create(route.getConfiguration(), KanbanConfig.class),
                configService.getConfiguration(),
                dialogFactoryRegistry,
                formCreator,
                detailRouteSetting,
                iconFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
