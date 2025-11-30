package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.Collections;

public class List<ModelClass, FieldType, RepositoryType> extends VerticalLayout {
    private final GenericEntityGrid<ModelClass, FieldType, RepositoryType> entityGrid;

    public List(Integer currentPathIndex,
                VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer = (RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);
        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RepositoryType dataStore = routeRenderer.dataStoreKey();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(routeRenderer, dataStore, context),
                null,
                null,
                routeHeader);

        // Render custom route actions if configured
        if (routeRenderer.routeActions() != null && !routeRenderer.routeActions().isEmpty()) {
            VortexCrudDataStore<FieldType, ModelClass> vortexDataStore =
                context.configService().configuration().dataStores().get(dataStore).dataStoreInstance();

            headerBar.renderActions(routeRenderer.routeActions(), contextConsumer -> {
                RouteActionContext<FieldType, ModelClass> ctx = RouteActionContext.<FieldType, ModelClass>builder()
                    .dataStore(vortexDataStore)
                    .selectedEntities(Collections.emptyList())  // No selection support yet
                    .refreshCallback(() -> UI.getCurrent().getPage().reload())
                    .viewComponent(this)
                    .build();
                contextConsumer.accept(ctx);
            });
        }

        SearchField textField = new SearchField(event -> applyFilter(event.getValue()));
        entityGrid = new GenericEntityGrid<>(routeResolver, routeRenderer, context);
        add(headerBar);
        if (routeRenderer.configuration() != null && routeRenderer.configuration().filterField() != null) {
            add(textField);
        }
        add(entityGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    private void applyFilter(String filterText) {
        DataProvider<Object, ?> dataProvider = entityGrid.getDataProvider();
        if (dataProvider != null) {
            if (dataProvider instanceof ConfigurableFilterDataProvider) {
                ((ConfigurableFilterDataProvider<?, Void, String>) dataProvider).setFilter(filterText);
            }
        }
    }

    private void onAdd(RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer, RepositoryType dataStore, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        Dialog dialog = routeRenderer.child().dialogFactoryInstance().create(
                null,
                null,
                null,
                routeRenderer.child(),
                null,
                dataStore,
                () -> UI.getCurrent().getPage().reload(),
                () -> {

                },
                context);
        dialog.open();
    }
}
