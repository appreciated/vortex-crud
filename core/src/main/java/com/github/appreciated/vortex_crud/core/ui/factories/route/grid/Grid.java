package com.github.appreciated.vortex_crud.core.ui.factories.route.grid;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components.EntityItemList;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public class Grid<ModelClass, FieldType, RepositoryType> extends VerticalLayout {
    private final VirtualItemGrid<ModelClass, FieldType, RepositoryType> virtualGrid;

    public Grid(VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer,
                VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry,
                VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry,
                VortexCrudItemFactoryRegistry<FieldType> itemFactoryRegistry,
                VortexCrudFileProviderRegistry fileProviderRegistry,
                VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                ReflectionService<FieldType> reflectionService,
                VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, routeRenderer, routeRenderer.dataStoreKey(), formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));
        virtualGrid = new VirtualItemGrid<>(routeResolver,
                routeRenderer,
                dataStoreFactoryRegistry,
                itemFactoryRegistry,
                fileProviderRegistry,
                resolver,
                reflectionService,
                dataStoreUtil
        );
        add(headerBar, search, virtualGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    private void applyFilter(String filterText) {
        DataProvider<EntityItemList<ModelClass>, ?> dataProvider = virtualGrid.getDataProvider();
        if (dataProvider != null) {
            if (dataProvider instanceof ConfigurableFilterDataProvider) {
                ((ConfigurableFilterDataProvider<?, Void, String>) dataProvider).setFilter(filterText);
            }
        }
    }

    private void onAdd(VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry,
                       RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer,
                       RepositoryType dataStore,
                       FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                       VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory) {
        Dialog dialog = dialogFactoryRegistry.getFactory(routeRenderer.child().factory()).create(
                null,
                null,
                null,
                routeRenderer.child(),
                null,
                dataStore,
                routeFactory,
                () -> UI.getCurrent().getPage().reload(),
                () -> {

                },
                formCreator);
        dialog.open();
    }
}

