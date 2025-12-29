package com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A custom VirtualList implementation for rendering entity items in a responsive grid layout with lazy loading.
 * This renderer dynamically adjusts the number of columns based on the browser window size and supports configuring card widths.
 * It also provides functionality for item click events and loading data from the database using a lazy loading approach.
 */

public class VirtualItemGrid<ModelClass, FieldType, RepositoryType> extends VirtualList<EntityItemList<ModelClass>> {

    private final VortexCrudItemFactory<FieldType> itemFactory;
    private final VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> pathVariables;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final RouteRenderer<ModelClass, FieldType, RepositoryType> config;
    private int minWidth = 250;  // Minimum width in pixels
    private int maxWidth = 350;  // Maximum width in pixels
    private int currentNumberOfColumns = -1;

    @SuppressWarnings("unchecked")
    public VirtualItemGrid(VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                           RouteRenderer<ModelClass, FieldType, RepositoryType> config,
                           VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        this.config = config;
        this.pathVariables = routeResolver;
        this.context = context;
        this.fieldNameResolver = context.fieldNameResolver();
        this.reflectionService = context.reflectionService();
        this.dataStoreUtil = context.dataStoreUtil();

        this.dataStore = (VortexCrudDataStore<FieldType, ?>) config.dataStoreConfig().dataStoreInstance();

        this.itemFactory = config.itemFactory();
        setSizeFull();
        this.addAttachListener(event -> {
            if (event.isInitialAttach()) {
                var ui = event.getUI();
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ui.access(this::onBrowserWindowResize);
                }).start();
            }
        });
    }

    private void initRenderer() {
        setRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout wrapper = new HorizontalLayout();
            wrapper.setSpacing(true);
            wrapper.setWidthFull();
            wrapper.getStyle().set("margin-bottom", "var(--vaadin-gap-s)");
            List<ModelClass> list = item.list();
            for (ModelClass entity : list) {
                Component component = itemFactory.renderItem(config,
                        entity,
                        maxWidth,
                        context);
                component.getStyle().setWidth("100%");
                Div div = new Div(component);
                div.getStyle()
                        .set("display", "flex")
                        .set("flex", "0 1 auto");

                div.setWidthFull();
                div.addClickListener(event -> onItemClick(entity));
                wrapper.add(div);
            }
            int roundedWith = Math.round((float) list.size() / currentNumberOfColumns * 100);
            wrapper.setWidth(roundedWith, Unit.PERCENTAGE);
            return wrapper;
        }));
    }

    private void onItemClick(Object entity) {
        String s = pathVariables.getPath() + "/" + dataStoreUtil.getId(entity);
        getUI().ifPresent(ui -> ui.navigate(s));
    }

    private void initLazyLoadingDataProvider() {
        CallbackDataProvider<EntityItemList<ModelClass>, String> dataProvider = DataProvider.fromFilteringCallbacks(
                // Fetching the data from the database for a given offset and limit
                query -> {
                    int offset = query.getOffset() * currentNumberOfColumns;
                    int limit = query.getLimit() * currentNumberOfColumns;
                    List<ModelClass> items;

                    // Fallback for non-queryable stores
                    boolean isQueryable = dataStore instanceof VortexCrudQueryDataStore;

                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        if (isQueryable && config.filters() != null && !config.filters().isEmpty()) {
                             items = (List<ModelClass>) ((VortexCrudQueryDataStore<FieldType, ?>)dataStore).getRecordsFromTableWhereFiltersEqual(config.filters(), offset, limit);
                        } else {
                            items = (List<ModelClass>) dataStore.getRecordsFromTable(offset, limit);
                        }
                    } else {
                        if (isQueryable) {
                            if (config.filters() != null && !config.filters().isEmpty()) {
                                items = (List<ModelClass>) ((VortexCrudQueryDataStore<FieldType, ?>)dataStore).getRecordsFromTableWhereColumnLikeAndFiltersEqual(config.titleField(), filterText, config.filters(), offset, limit);
                            } else {
                                items = (List<ModelClass>) ((VortexCrudQueryDataStore<FieldType, ?>)dataStore).getRecordsFromTableWhereColumnLike(config.titleField(), filterText, offset, limit);
                            }
                        } else {
                            // Fallback: fetch all and filter in memory? Not feasible for virtual list.
                            // Return all paginated (ignoring filter) or empty list?
                            // For simplicity, let's return paginated results ignoring filter (User should know if store supports filtering)
                            items = (List<ModelClass>) dataStore.getRecordsFromTable(offset, limit);
                        }
                    }

                    List<EntityItemList<ModelClass>> wrappers = new ArrayList<>();
                    for (int i = 0; i < items.size(); i += currentNumberOfColumns) {
                        List<ModelClass> group = items.subList(i, Math.min(i + currentNumberOfColumns, items.size()));
                        wrappers.add(new EntityItemList<>(group));
                    }

                    return wrappers.stream();
                },
                // Providing the total number of records for correct pagination
                query -> {
                    int count;
                    boolean isQueryable = dataStore instanceof VortexCrudQueryDataStore;

                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        if (isQueryable && config.filters() != null && !config.filters().isEmpty()) {
                             count = ((VortexCrudQueryDataStore<FieldType, ?>)dataStore).countWhereFiltersEqual(config.filters());
                        } else {
                            count = dataStore.count();
                        }
                    } else {
                        if (isQueryable) {
                             if (config.filters() != null && !config.filters().isEmpty()) {
                                 count = ((VortexCrudQueryDataStore<FieldType, ?>)dataStore).countWhereColumnLikeAndFiltersEqual(config.titleField(), filterText, config.filters());
                             } else {
                                 count = ((VortexCrudQueryDataStore<FieldType, ?>)dataStore).countWhereColumnLike(config.titleField(), filterText);
                             }
                        } else {
                            count = dataStore.count();
                        }
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
