package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import com.github.appreciated.vortex_crud.core.context.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
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

public class KanbanView<ModelClass, FieldType, RepositoryType> extends VerticalLayout {

    private final VortexCrudItemFactory<FieldType> itemFactory;
    private final KanbanConfiguration<ModelClass, FieldType, RepositoryType> kanbanConfigurationConfig;
    private final ComponentRenderer<Component, Object> itemRenderer;
    private final RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer;
    private final VortexCrudDataStore<FieldType, Object> dataStore;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver;
    private final Map<Object, Grid<Object>> columns = new HashMap<>();
    private final Map<Object, ConfigurableFilterDataProvider<Object, Void, String>> columnProviders = new HashMap<>();

    private final GenericFilterableDataProvider<FieldType> dataProvider;

    private Object draggedItem;
    private Grid<Object> dragSource;

    public KanbanView(RepositoryType dataStoreIdentifier,
                      RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                      VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                      VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                      DetailRouteSetting detailRouteSetting
    ) {
        this.routeRenderer = routeRenderer;
        this.context = context;
        this.dataStore = (VortexCrudDataStore<FieldType, Object>) routeRenderer.dataStoreInstance();
        this.fieldNameResolver = context.fieldNameResolver();
        this.reflectionService = context.reflectionService();
        this.dataStoreUtil = context.dataStoreUtil();
        this.routeResolver = routeResolver;

        Selects selects = context.configService().configuration().selects();
        DataStoreConfig<ModelClass, FieldType, RepositoryType> config = routeRenderer.dataStoreConfig();
        this.kanbanConfigurationConfig = (KanbanConfiguration<ModelClass, FieldType, RepositoryType>) routeRenderer.configuration();
        Field<ModelClass, FieldType, RepositoryType> dataStoreField = config.fields().get(kanbanConfigurationConfig.columnField());

        this.itemFactory = kanbanConfigurationConfig.factory();

        dataProvider = new GenericFilterableDataProvider<>(this.dataStore, kanbanConfigurationConfig.filterField());

        itemRenderer = new ComponentRenderer<>(entity -> {
            Div cardWrapper = new Div(itemFactory.renderItem(kanbanConfigurationConfig,
                    entity,
                    null,
                    context));
            cardWrapper.getStyle().set("margin", "5px 0");
            cardWrapper.addClickListener(event -> {
                String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), dataStoreUtil.getId(entity));
                getUI().ifPresent(ui -> ui.navigate(nextRoute));
            });
            return cardWrapper;
        });

        Object selectName = ((SelectField<ModelClass, FieldType, RepositoryType>) dataStoreField).values();
        Map<?, String> selectConfig = selects.configs().get(selectName);

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
                event -> onAdd(context, (RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeRenderer, (VortexCrudDataStore<FieldType, ModelClass>) dataStore),
                null,
                null,
                routeHeader);

        // Render custom route actions if configured
        if (routeRenderer.routeActions() != null && !routeRenderer.routeActions().isEmpty()) {
            headerBar.renderActions(routeRenderer.routeActions(), contextConsumer -> {
                RouteActionContext<FieldType, ModelClass> actionContext = RouteActionContext.<FieldType, ModelClass>builder()
                    .dataStore((VortexCrudDataStore<FieldType, ModelClass>) dataStore)
                    .selectedEntities(java.util.Collections.emptyList())  // No selection support yet
                    .refreshCallback(() -> UI.getCurrent().getPage().reload())
                    .viewComponent(this)
                    .build();
                contextConsumer.accept(actionContext);
            });
        }

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

    private void openDialog(
                            RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer,
                            VortexCrudDataStore<FieldType, ?> dataStore,
                            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                            Object entity) {
        // Navigate to the entity URL
        if (routeRenderer.child() != null && routeRenderer.child().dialogFactory() != null) {
            Dialog dialog = routeRenderer.child().dialogFactory().create(
                    context.dataStoreUtil().getId(entity),
                    null,
                    null,
                    routeRenderer.child(),
                    null,
                    (VortexCrudDataStore<FieldType, ModelClass>) dataStore,
                    context,
                    () -> {
                        Object recordById = dataStore.getRecordById(context.dataStoreUtil().getId(entity));
                        this.dataStore.updateRecordById(recordById);
                        refreshColumns();
                        String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), null);
                        Optional<UI> ui1 = getUI();
                        ui1.ifPresent(ui -> ui.navigate(nextRoute));
                    },
                    () -> {
                        String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), null);
                        Optional<UI> ui1 = getUI();
                        ui1.ifPresent(ui -> ui.navigate(nextRoute));
                    });
            dialog.open();
        }
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
        int i = currentIndex + 1;
        if (routeResolver.hasPathForIndex(i)) {
            String pathForIndex = routeResolver.getPathForIndex(i);
            Object recordById = dataStore.getRecordById(pathForIndex);
            openDialog(
                    (RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeRenderer,
                    dataStore,
                    context,
                    routeResolver,
                    recordById
            );
        }
    }

    private VerticalLayout createColumn(String title, Object columnDatabaseValue) {
        Grid<Object> grid = new Grid<>();
        grid.getStyle().set("--vaadin-grid-cell-padding", "0");
        grid.getStyle().set("--vaadin-background-color", "transparent");
        grid.setHeight("90%");
        grid.setWidthFull();
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
                                            reflectionService.getValue(item, kanbanConfigurationConfig.columnField()),
                                            columnDatabaseValue));
                            if (kanbanConfigurationConfig.rowIndexField() != null) {
                                stream = stream.sorted(Comparator.comparing(o -> (Comparable) reflectionService.getValue(o, kanbanConfigurationConfig.rowIndexField())));
                            }
                            return stream.skip(query.getOffset()).limit(query.getLimit());
                        },
                        query -> (int) dataProvider.fetch(new Query<>(0, 1000, Collections.emptyList(), null, query.getFilter().orElse(null)))
                                .filter(item -> Objects.equals(
                                        reflectionService.getValue(item, kanbanConfigurationConfig.columnField()),
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
            Object originalColumnValue = reflectionService.getValue(draggedItem, kanbanConfigurationConfig.columnField());

            // Update column value
            reflectionService.setValue(draggedItem, kanbanConfigurationConfig.columnField(), columnDatabaseValue);

            if (kanbanConfigurationConfig.rowIndexField() != null) {
                // Get fresh items in target column
                List<Object> targetColumnItems = dataStore.getRecordsFromTableWhereColumnEqualsOrdered(
                        kanbanConfigurationConfig.columnField(),
                        columnDatabaseValue,
                        kanbanConfigurationConfig.rowIndexField(),
                        0,
                        1000
                );

                Integer newPosition;

                if (!event.getDropTargetItem().isPresent()) {
                    // Dropping at the end
                    Integer lastPosition = targetColumnItems.isEmpty() ? null :
                            (Integer) reflectionService.getValue(targetColumnItems.get(targetColumnItems.size() - 1),
                                    kanbanConfigurationConfig.rowIndexField());
                    newPosition = generateKeyBetween(lastPosition, null);
                } else {
                    Object targetItem = event.getDropTargetItem().get();
                    Integer targetPosition = (Integer) reflectionService.getValue(targetItem,
                            kanbanConfigurationConfig.rowIndexField());

                    if (event.getDropLocation() == GridDropLocation.BELOW) {
                        // Find next item's position
                        int targetIndex = targetColumnItems.indexOf(targetItem);
                        Integer nextPosition = (targetIndex < targetColumnItems.size() - 1) ?
                                (Integer) reflectionService.getValue(targetColumnItems.get(targetIndex + 1),
                                        kanbanConfigurationConfig.rowIndexField()) : null;
                        newPosition = generateKeyBetween(targetPosition, nextPosition);
                    } else {
                        // Find previous item's position
                        int targetIndex = targetColumnItems.indexOf(targetItem);
                        Integer prevPosition = (targetIndex > 0) ?
                                (Integer) reflectionService.getValue(targetColumnItems.get(targetIndex - 1),
                                        kanbanConfigurationConfig.rowIndexField()) : null;
                        newPosition = generateKeyBetween(prevPosition, targetPosition);
                    }
                }

                // Update the dragged item with new position
                try {
                    reflectionService.setValue(draggedItem, kanbanConfigurationConfig.rowIndexField(), newPosition);
                } catch (Exception e) {
                    // Ignore exception if setting row index fails
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
        wrapper.setPadding(false);

        Div titleLabel = new Div(new H4(title));
        titleLabel.getStyle().set("font-weight", "bold").set("margin-bottom", "10px");
        wrapper.add(titleLabel, grid);
        return wrapper;
    }

    private void onAdd(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                       RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer,
                       VortexCrudDataStore<FieldType, ModelClass> dataStore) {
        Object entity = new Object(); // This seems wrong? dataStore.newInstance()? But entity is not used in create...
        // Ah, previous code: Object entity = new Object();
        // create call:
        // create(null, null, null, ...)

        if (routeRenderer.child() != null && routeRenderer.child().dialogFactory() != null) {
            Dialog dialog = routeRenderer.child().dialogFactory().create(
                    null,
                    null,
                    null,
                    routeRenderer.child(),
                    null,
                    dataStore,
                    context,
                    () -> {
                        // Object recordById = this.dataStore.getRecordById(dataStoreUtil.getId(entity)); // Entity is empty Object?
                        // This onAdd looks suspicious in original code too.
                        // But I'll preserve logic.
                        // Wait, previous logic was:
                        /*
                        Object entity = new Object();
                        Dialog dialog = ... .create(
                             null, ...
                             () -> {
                                Object recordById = this.dataStore.getRecordById(dataStoreUtil.getId(entity));
                         */
                        // If entity is new Object(), getId(entity) will fail or return null?
                        // If create(...) creates a new record, it doesn't return it via callback.
                        // The callback updates columns.

                        // I'll stick to refreshColumns().
                        refreshColumns();
                    },
                    () -> {

                    });
            dialog.open();
        }
    }
}
