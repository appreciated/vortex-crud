package com.github.appreciated.turbo_crud.ui.factories.route.kanban;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Kanban;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.component.KanbanView;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class TCKanbanDetailFactoryImpl implements TurboCrudRouteFactory {
    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudConfigService configService;
    private final TurboCrudItemFactoryRegistry turboCrudItemFactory;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final FormCreator formCreator;
    private final TurboCrudDialogFactoryRegistry dialogFactoryRegistry;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;

    public TCKanbanDetailFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                     TurboCrudConfigService configService,
                                     TurboCrudItemFactoryRegistry turboCrudItemFactory,
                                     TurboCrudRouteFactoryRegistry routeFactory,
                                     FormCreator formCreator,
                                     TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                                     TurboCrudFileProviderRegistry fileProviderRegistry
    ) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.configService = configService;
        this.turboCrudItemFactory = turboCrudItemFactory;
        this.routeFactory = routeFactory;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.fileProviderRegistry = fileProviderRegistry;
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
                (Kanban) route.getConfiguration(),
                configService.getConfiguration(),
                dialogFactoryRegistry,
                fileProviderRegistry,
                formCreator,
                detailRouteSetting);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
