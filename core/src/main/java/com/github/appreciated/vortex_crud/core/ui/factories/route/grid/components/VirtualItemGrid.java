package com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.GridOrListRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom VirtualList implementation for rendering entity items in a responsive grid layout with lazy loading.
 * This renderer dynamically adjusts the number of columns based on the browser window size and supports configuring card widths.
 * It also provides functionality for item click events and loading data from the database using a lazy loading approach.
 */

public class VirtualItemGrid<DataStoreId, FieldId> extends VirtualList<EntityItemList> {

    private final VortexCrudItemFactory<FieldId> itemFactory;
    private final VortexCrudPathToRouteResolver<DataStoreId, FieldId> pathVariables;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final VortexCrudDataStore<FieldId> dataStore;
    private final GridOrListRendererConfiguration<DataStoreId, FieldId> gridOrListConfiguration;
    private int minWidth = 250;  // Minimum width in pixels
    private int maxWidth = 350;  // Maximum width in pixels
    private int currentNumberOfColumns = -1;

    public VirtualItemGrid(VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                           RouteRenderer<DataStoreId, FieldId> config,
                           VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                           VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                           VortexCrudFileProviderRegistry fileProviderRegistry,
                           VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
    ) {
        this.pathVariables = routeResolver;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
        DataStoreId table = config.getDataStore();

        this.dataStore = dataStoreFactoryRegistry.getDataStore(table);
        gridOrListConfiguration = (GridOrListRendererConfiguration<DataStoreId, FieldId>) config.getConfiguration();

        this.itemFactory = itemFactoryRegistry.getFactory(gridOrListConfiguration.getFactory());
        setSizeFull();
        this.addAttachListener(event -> {
            if (event.isInitialAttach()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    event.getUI().access(this::onBrowserWindowResize);
                }).start();
            }
        });
    }

    private void initRenderer() {
        setRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout wrapper = new HorizontalLayout();
            wrapper.setSpacing(true);
            wrapper.setWidthFull();
            List<GenericEntity> list = item.getList();
            for (GenericEntity entity : list) {
                Div div = new Div(itemFactory.renderItem(gridOrListConfiguration, entity, maxWidth, fileProviderRegistry, fieldNameResolver));
                div.getStyle()
                        .set("display", "flex")
                        .set("flex", "0 1 auto")
                        .set("flex", "0 1 auto")
                        .set("overflow", "hidden");
                div.setWidthFull();
                div.addClickListener(event -> onItemClick(entity));
                wrapper.add(div);
            }
            wrapper.getStyle().set("padding", "10px 0px 0px 0px");
            int roundedWith = Math.round((float) list.size() / currentNumberOfColumns * 100);
            wrapper.setWidth(roundedWith, Unit.PERCENTAGE);
            return wrapper;
        }));
    }

    private void onItemClick(GenericEntity entity) {
        String s = pathVariables.getPath() + "/" + DataStoreUtil.getId(entity);
        getUI().ifPresent(ui -> ui.navigate(s));
    }

    private void initLazyLoadingDataProvider() {
        CallbackDataProvider<EntityItemList, String> dataProvider = DataProvider.fromFilteringCallbacks(
                // Fetching the data from the database for a given offset and limit
                query -> {
                    int offset = query.getOffset() * currentNumberOfColumns;
                    int limit = query.getLimit() * currentNumberOfColumns;
                    List<GenericEntity> items;

                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        items = dataStore.getRecordsFromTable(offset, limit);
                    } else {
                        items = dataStore.getRecordsFromTableWhereColumnLike(gridOrListConfiguration.getTitleField(), filterText, offset, limit);
                    }

                    List<EntityItemList> wrappers = new ArrayList<>();
                    for (int i = 0; i < items.size(); i += currentNumberOfColumns) {
                        List<GenericEntity> group = items.subList(i, Math.min(i + currentNumberOfColumns, items.size()));
                        wrappers.add(new EntityItemList(group));
                    }

                    return wrappers.stream();
                },
                // Providing the total number of records for correct pagination
                query -> {
                    int count;
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        count = dataStore.count();
                    } else {
                        count = dataStore.countWhereColumnLike(gridOrListConfiguration.getTitleField(), filterText);
                    }
                    return (int) Math.ceil((double) count / (double) currentNumberOfColumns);
                }
        );
        this.setDataProvider(dataProvider.withConfigurableFilter());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attachEvent.getUI().getPage().addBrowserWindowResizeListener(e -> onBrowserWindowResize());
    }

    /**
     * Sets the minimum and maximum width for the cards.
     *
     * @param minWidth Minimum width in pixels
     * @param maxWidth Maximum width in pixels
     */
    public void setCardWidth(int minWidth, int maxWidth) {
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        onBrowserWindowResize();
    }

    /**
     * Method to dynamically adjust the layout based on the width of the container.
     */
    private void onBrowserWindowResize() {
        PendingJavaScriptResult containerWidthResult = getElement().executeJs("return $0.clientWidth;", getElement());
        containerWidthResult.then(Double.class, containerWidth -> {
            if (containerWidth != null) {
                // Berechne die Anzahl der Spalten basierend auf der Breite des Containers
                int newNumberOfColumns = Math.max(1, (int) Math.floor(containerWidth / minWidth));
                if (newNumberOfColumns != currentNumberOfColumns) {
                    currentNumberOfColumns = newNumberOfColumns;
                    initRenderer();
                    initLazyLoadingDataProvider();
                }
            }
        });
    }
}
