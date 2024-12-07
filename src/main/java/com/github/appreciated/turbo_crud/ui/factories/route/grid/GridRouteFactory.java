package com.github.appreciated.turbo_crud.ui.factories.route.grid;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class GridRouteFactory implements TurboCrudRouteFactory {

    private final TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry;
    private final FormCreator formCreator;
    private final TurboCrudDialogFactoryRegistry dialogFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry routeFactoryRegistry;
    private final TurboCrudItemFactoryRegistry itemFactoryRegistry;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;

    public GridRouteFactory(
            TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry,
            FormCreator formCreator,
            TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
            TurboCrudRouteFactoryRegistry routeFactoryRegistry,
            TurboCrudItemFactoryRegistry itemFactoryRegistry,
            TurboCrudFileProviderRegistry fileProviderRegistry
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {

        Route route = routeResolver.getRouteForIndex(currentPathIndex);

        return new Grid(routeResolver,
                route,
                dataStoreFactoryRegistry,
                formCreator,
                dialogFactoryRegistry,
                routeFactoryRegistry,
                itemFactoryRegistry,
                fileProviderRegistry);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}