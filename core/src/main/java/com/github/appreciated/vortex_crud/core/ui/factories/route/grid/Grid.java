package com.github.appreciated.vortex_crud.core.ui.factories.route.grid;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
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

import java.util.Collections;

public class Grid<ModelClass, FieldType, RepositoryType> extends VerticalLayout {
    private final VirtualItemGrid<ModelClass, FieldType, RepositoryType> virtualGrid;

    public Grid(VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer,
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
                event -> onAdd(dialogFactoryRegistry, routeRenderer, routeRenderer.dataStoreInstance(), formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);

        // Render custom route actions if configured
        if (routeRenderer.routeActions() != null && !routeRenderer.routeActions().isEmpty()) {
            VortexCrudDataStore<FieldType, ModelClass> dataStore = routeRenderer.dataStoreInstance();

            headerBar.renderActions(routeRenderer.routeActions(), contextConsumer -> {
                RouteActionContext<FieldType, ModelClass> context = RouteActionContext.<FieldType, ModelClass>builder()
                    .dataStore(dataStore)
                    .selectedEntities(Collections.emptyList())  // No selection support yet
                    .refreshCallback(() -> UI.getCurrent().getPage().reload())
                    .viewComponent(this)
                    .build();
                contextConsumer.accept(context);
            });
        }

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));
        virtualGrid = new VirtualItemGrid<>(routeResolver,
                routeRenderer,
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
                       VortexCrudDataStore<FieldType, ModelClass> dataStore,
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

