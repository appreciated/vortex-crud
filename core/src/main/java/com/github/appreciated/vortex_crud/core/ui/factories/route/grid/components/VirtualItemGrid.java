package com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.GridItemRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
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
import java.util.List;

/**
 * A custom VirtualList implementation for rendering entity items in a responsive grid layout with lazy loading.
 * This renderer dynamically adjusts the number of columns based on the browser window size and supports configuring card widths.
 * It also provides functionality for item click events and loading data from the database using a lazy loading approach.
 */

public class VirtualItemGrid<ModelClass, FieldType, RepositoryType> extends VirtualList<EntityItemList<ModelClass>> {

    private final VortexCrudItemFactory<FieldType> itemFactory;
    private final VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> pathVariables;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private final GridItemRendererConfiguration<ModelClass, FieldType, RepositoryType> itemRendererConfiguration;
    private int minWidth = 250;  // Minimum width in pixels
    private int maxWidth = 350;  // Maximum width in pixels
    private int currentNumberOfColumns = -1;

    @SuppressWarnings("unchecked")
    public VirtualItemGrid(VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                           RouteRenderer<ModelClass, FieldType, RepositoryType> config,
                           VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                           VortexCrudItemFactoryRegistry<FieldType> itemFactoryRegistry,
                           VortexCrudFileProviderRegistry fileProviderRegistry,
                           VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
                           ReflectionService<FieldType> reflectionService,
                           VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.pathVariables = routeResolver;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
        RepositoryType table = config.dataStoreKey();

        this.dataStore = dataStoreFactoryRegistry.getDataStore(table);
        itemRendererConfiguration = (GridItemRendererConfiguration<ModelClass, FieldType, RepositoryType>) config.configuration();

        this.itemFactory = itemFactoryRegistry.getFactory(itemRendererConfiguration.factory());
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
            List<ModelClass> list = item.list();
            for (ModelClass entity : list) {
                Component component = itemFactory.renderItem(itemRendererConfiguration,
                        entity,
                        maxWidth,
                        fileProviderRegistry,
                        fieldNameResolver,
                        reflectionService);
                component.getStyle().setWidth("100%");
                Div div = new Div(component);
                div.getStyle()
                        .set("display", "flex")
                        .set("flex", "0 1 auto")
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

                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        items = (List<ModelClass>) dataStore.getRecordsFromTable(offset, limit);
                    } else {
                        items = (List<ModelClass>) dataStore.getRecordsFromTableWhereColumnLike(itemRendererConfiguration.titleField(), filterText, offset, limit);
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
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        count = dataStore.count();
                    } else {
                        count = dataStore.countWhereColumnLike(itemRendererConfiguration.titleField(), filterText);
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
