package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.*;

import static com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component.FractionalIndex.generateKeyBetween;

public class KanbanView<DataStoreId, FieldId, KeyType> extends VerticalLayout {

    private final VortexCrudItemFactory<FieldId> itemFactory;
    private final Kanban<DataStoreId, FieldId, KeyType> kanbanConfig;
    private final ComponentRenderer<Component, Object> itemRenderer;
    private final KeyType dataStoreIdentifier;
    private final RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer;
    private final VortexCrudDataStore<FieldId, Object> dataStore;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final FormCreator<DataStoreId, FieldId, KeyType> formCreator;
    private final ReflectionService<FieldId> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver;
    private final Map<Object, Grid<Object>> columns = new HashMap<>();
    private final Map<Object, ConfigurableFilterDataProvider<Object, Void, String>> columnProviders = new HashMap<>();

    private final GenericFilterableDataProvider<FieldId> dataProvider;

    private Object draggedItem;
    private Grid<Object> dragSource;

    public KanbanView(KeyType dataStoreIdentifier,
                      RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                      VortexCrudDataStore<FieldId, ?> dataStore,
                      VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                      VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                      Kanban<DataStoreId, FieldId, KeyType> kanbanConfig,
                      Application<DataStoreId, FieldId, KeyType> configService,
                      VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                      VortexCrudFileProviderRegistry fileProviderRegistry,
                      VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                      FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                      DetailRouteSetting detailRouteSetting,
                      ReflectionService<FieldId> reflectionService,
                      VortexCrudDataStoreUtilStrategy dataStoreUtil,
                      VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver
    ) {
        this.dataStoreIdentifier = dataStoreIdentifier;
        this.routeRenderer = routeRenderer;
        this.dataStore = (VortexCrudDataStore<FieldId, Object>) dataStore;
        this.routeFactory = routeFactory;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.formCreator = formCreator;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
        this.routeResolver = routeResolver;
        Selects selects = configService.getSelects();
        DataStoreConfig<DataStoreId, FieldId, KeyType> config = configService.getDataStores().get(dataStoreIdentifier);
        Field<DataStoreId, FieldId, KeyType> dataStoreField = config.getFields().get(kanbanConfig.getColumnField());

        this.kanbanConfig = kanbanConfig;
        this.itemFactory = itemFactoryRegistry.getFactory(kanbanConfig.getFactory());
        this.fileProviderRegistry = fileProviderRegistry;

        dataProvider = new GenericFilterableDataProvider<>(this.dataStore, kanbanConfig.getFilterField());

        itemRenderer = new ComponentRenderer<>(entity -> {
            Div cardWrapper = new Div(itemFactory.renderItem(kanbanConfig,
                    entity,
                    null,
                    fileProviderRegistry,
                    fieldNameResolver,
                    reflectionService));
            cardWrapper.addClickListener(event -> {
                String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex() + 1, dataStoreUtil.getId(entity));
                getUI().ifPresent(ui -> ui.navigate(nextRoute));
            });
            return cardWrapper;
        });

        Object selectName = dataStoreField.getValues();
        Map<?, String> selectConfig = selects.getConfigs().get(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<?> strings = selectConfig.keySet();

        HorizontalLayout kanbanBoard = new HorizontalLayout();

        kanbanBoard.getStyle()
                .set("flex", "1 1 auto")
                .set("overflow", "auto");

        for (Object string : strings) {
            VerticalLayout columnWrapper = createColumn(getTranslation(selectConfig.get(string)), string);
            kanbanBoard.add(columnWrapper);
        }
        refreshColumns();
        kanbanBoard.setSizeFull();

        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, routeRenderer, dataStoreIdentifier, formCreator, routeFactory),
                null,
                null,
                routeHeader);

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));

        if (!detailRouteSetting.isHeaderHidden()) {
            add(headerBar, search);
        } else {
            add(search);
        }

        add(kanbanBoard);
        setSizeFull();
        setPadding(true);
    }

    private void openDialog(KeyType dataStoreIdentifier, RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer, VortexCrudDataStore<FieldId, ?> dataStore, VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory, Kanban<DataStoreId, FieldId, KeyType> kanbanConfig, VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry, VortexCrudFileProviderRegistry fileProviderRegistry, VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver, FormCreator<DataStoreId, FieldId, KeyType> formCreator, ReflectionService<FieldId> reflectionService, VortexCrudDataStoreUtilStrategy dataStoreUtil, VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver, Object entity) {
        // Navigate to the entity URL
        Dialog dialog = dialogFactoryRegistry.getFactory(routeRenderer.getChild().getFactory()).create(
                dataStoreUtil.getId(entity),
                null,
                null,
                routeRenderer.getChild(),
                null,
                dataStoreIdentifier,
                routeFactory,
                () -> {
                    Object recordById = dataStore.getRecordById(dataStoreUtil.getId(entity));
                    this.dataStore.updateRecordById(recordById);
                    refreshColumns();
                    String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex() + 1, null);
                    Optional<UI> ui1 = getUI();
                    ui1.ifPresent(ui -> ui.navigate(nextRoute));
                },
                () -> {
                    String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex() + 1, null);
                    Optional<UI> ui1 = getUI();
                    ui1.ifPresent(ui -> ui.navigate(nextRoute));
                },
                formCreator);
        dialog.open();
    }

    private void refreshColumns() {
        columnProviders.values().forEach(ConfigurableFilterDataProvider::refreshAll);
    }

    private void applyFilter(String filterText) {
        columnProviders.values().forEach(provider -> provider.setFilter(filterText));
        refreshColumns();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        Integer currentIndex = routeResolver.determineActiveRouteIndex();
        int i = currentIndex + 2;
        if (routeResolver.hasPathForIndex(i)) {
            String pathForIndex = routeResolver.getPathForIndex(i);
            Object recordById = dataStore.getRecordById(pathForIndex);
            openDialog(
                    dataStoreIdentifier,
                    routeRenderer,
                    dataStore,
                    routeFactory,
                    kanbanConfig,
                    dialogFactoryRegistry,
                    fileProviderRegistry,
                    fieldNameResolver,
                    formCreator,
                    reflectionService,
                    dataStoreUtil,
                    routeResolver,
                    recordById
            );
        }
    }

    private VerticalLayout createColumn(String title, Object columnDatabaseValue) {
        Grid<Object> grid = new Grid<>();
        grid.getStyle().set("--vaadin-grid-cell-padding", "0");
        grid.getStyle().set("--lumo-base-color", "transparent");
        grid.setHeight("90%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addComponentColumn(itemRenderer::createComponent).setWidth("100%").setFlexGrow(1);
        grid.setAllRowsVisible(true);

        ConfigurableFilterDataProvider<Object, Void, String> provider =
                DataProvider.<Object, String>fromFilteringCallbacks(query -> {
                            Query<Object, String> baseQuery = new Query<>(0, 1000, Collections.emptyList(), null, query.getFilter().orElse(null));
                            java.util.stream.Stream<Object> stream = dataProvider.fetch(baseQuery)
                                    .filter(item -> Objects.equals(
                                            reflectionService.getValue(item, kanbanConfig.getColumnField()),
                                            columnDatabaseValue));
                            if (kanbanConfig.getRowIndexField() != null) {
                                stream = stream.sorted(Comparator.comparing(o -> (Comparable) reflectionService.getValue(o, kanbanConfig.getRowIndexField())));
                            }
                            return stream.skip(query.getOffset()).limit(query.getLimit());
                        },
                        query -> (int) dataProvider.fetch(new Query<>(0, 1000, Collections.emptyList(), null, query.getFilter().orElse(null)))
                                .filter(item -> Objects.equals(
                                        reflectionService.getValue(item, kanbanConfig.getColumnField()),
                                        columnDatabaseValue))
                                .count()
                ).withConfigurableFilter();

        grid.setDataProvider(provider);
        columnProviders.put(columnDatabaseValue, provider);

        // Enable dragging rows
        grid.setRowsDraggable(true);
        // Drop mode is enabled on drag start across all grids
        grid.setDropMode(null);

        grid.addDragStartListener(e -> {
            if (e.getDraggedItems() == null || e.getDraggedItems().isEmpty()) {
                return;
            }
            draggedItem = e.getDraggedItems().get(0);
            dragSource = grid;
            // Allow dropping anywhere on the grid, including empty areas
            columns.values().forEach(g -> g.setDropMode(GridDropMode.BETWEEN));
        });

// Replace the grid.addDropListener implementation with this updated version
        grid.addDropListener(event -> {
            if (draggedItem == null || dragSource == null) {
                return;
            }

            // Store original column value
            Object originalColumnValue = reflectionService.getValue(draggedItem, kanbanConfig.getColumnField());

            // Update column value
            reflectionService.setValue(draggedItem, kanbanConfig.getColumnField(), columnDatabaseValue);

            if (kanbanConfig.getRowIndexField() != null) {
                // Get fresh items in target column
                List<Object> targetColumnItems = dataStore.getRecordsFromTableWhereColumnEquals(
                        kanbanConfig.getColumnField(),
                        columnDatabaseValue,
                        0,
                        1000
                );

                Integer newPosition;

                if (!event.getDropTargetItem().isPresent()) {
                    // Dropping at the end
                    Integer lastPosition = targetColumnItems.isEmpty() ? null :
                            (Integer) reflectionService.getValue(targetColumnItems.get(targetColumnItems.size() - 1),
                                    kanbanConfig.getRowIndexField());
                    newPosition = generateKeyBetween(lastPosition, null);
                } else {
                    Object targetItem = event.getDropTargetItem().get();
                    Integer targetPosition = (Integer) reflectionService.getValue(targetItem,
                            kanbanConfig.getRowIndexField());

                    if (event.getDropLocation() == GridDropLocation.BELOW) {
                        // Find next item's position
                        int targetIndex = targetColumnItems.indexOf(targetItem);
                        Integer nextPosition = (targetIndex < targetColumnItems.size() - 1) ?
                                (Integer) reflectionService.getValue(targetColumnItems.get(targetIndex + 1),
                                        kanbanConfig.getRowIndexField()) : null;
                        newPosition = generateKeyBetween(targetPosition, nextPosition);
                    } else {
                        // Find previous item's position
                        int targetIndex = targetColumnItems.indexOf(targetItem);
                        Integer prevPosition = (targetIndex > 0) ?
                                (Integer) reflectionService.getValue(targetColumnItems.get(targetIndex - 1),
                                        kanbanConfig.getRowIndexField()) : null;
                        newPosition = generateKeyBetween(prevPosition, targetPosition);
                    }
                }

                // Update the dragged item with new position
                try {
                    reflectionService.setValue(draggedItem, kanbanConfig.getRowIndexField(), newPosition);
                } catch (Exception e) {
                    System.out.println();
                }
            }
            dataStore.updateRecordById(draggedItem);
            refreshColumns();
        });

        grid.addDragEndListener(e -> {
            draggedItem = null;
            dragSource = null;
            // Reset drop modes after drag ends
            columns.values().forEach(g -> g.setDropMode(null));
        });

        columns.put(columnDatabaseValue, grid);

        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setHeightFull();
        wrapper.setWidth("300px");
        wrapper.getStyle()
                .set("flex", "0 0 auto")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("border-radius", "var(--lumo-border-radius-l)");
        wrapper.addClassNames("no-hover");
        wrapper.setSpacing(false);
        wrapper.setPadding(true);

        Div titleLabel = new Div(new H4(title));
        titleLabel.getStyle().set("font-weight", "bold").set("margin-bottom", "10px");
        wrapper.add(titleLabel, grid);
        return wrapper;
    }

    private void onAdd(VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                       RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                       KeyType dataStore,
                       FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                       VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory) {
        Object entity = new Object();
        Dialog dialog = dialogFactoryRegistry.getFactory(routeRenderer.getChild().getFactory()).create(
                null,
                null,
                null,
                routeRenderer.getChild(),
                null,
                dataStore,
                routeFactory,
                () -> {
                    Object recordById = this.dataStore.getRecordById(dataStoreUtil.getId(entity));
                    this.dataStore.updateRecordById(recordById);
                    refreshColumns();
                },
                () -> {

                },
                formCreator);
        dialog.open();
    }
}
