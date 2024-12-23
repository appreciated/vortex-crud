package com.github.appreciated.turbo_crud.core.ui.factories.route.grid.components;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.GridOrListConfiguration;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.vaadin.flow.component.AttachEvent;
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

public class VirtualItemGrid extends VirtualList<EntityItemList> {

    private final TurboCrudItemFactory itemFactory;
    private final TurboCrudPathToRouteResolver pathVariables;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;
    private final TurboCrudDataStore dataStore;
    private final GridOrListConfiguration gridOrListConfiguration;
    private int minWidth = 250;  // Minimum width in pixels
    private int maxWidth = 350;  // Maximum width in pixels
    private int currentNumberOfColumns = -1;

    public VirtualItemGrid(TurboCrudPathToRouteResolver routeResolver,
                           Route config,
                           TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry,
                           TurboCrudItemFactoryRegistry itemFactoryRegistry,
                           TurboCrudFileProviderRegistry fileProviderRegistry) {
        this.pathVariables = routeResolver;
        this.fileProviderRegistry = fileProviderRegistry;
        String table = config.getDataStore();

        this.dataStore = dataStoreFactoryRegistry.getFactory(table);
        gridOrListConfiguration = (GridOrListConfiguration) config.getConfiguration();

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
            HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.setWidthFull();
            for (GenericEntity entity : item.getList()) {
                Div div = new Div(itemFactory.renderItem(gridOrListConfiguration, entity, maxWidth, fileProviderRegistry));
                div.getStyle().set("display", "flex");
                div.addClickListener(event -> onItemClick(entity));
                layout.add(div);
            }
            wrapper.add(layout);
            wrapper.getStyle().set("padding", "10px 0px 0px 0px");
            return wrapper;
        }));
    }

    private void onItemClick(GenericEntity entity) {
        getUI().ifPresent(ui -> ui.navigate("/view/" + pathVariables.getPath() + "/" + DataStoreUtil.getId(entity)));
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
                    for (int i = 0; i < Math.ceil((double) items.size() / (double) currentNumberOfColumns); i++) {
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
