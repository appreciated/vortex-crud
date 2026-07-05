package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
    private final KanbanRoute<ModelClass, FieldType, RepositoryType> kanbanRoute;
    private final ComponentRenderer<Component, Object> itemRenderer;
    private final RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer;
    private final VortexCrudDataStore<FieldType, Object> dataStore;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final  VortexCrudPathToRouteResolver routeResolver;
    private final Map<Object, Grid<Object>> columns = new HashMap<>();
    private final Map<Object, ConfigurableFilterDataProvider<Object, Void, String>> columnProviders = new HashMap<>();

    private final GenericFilterableDataProvider<FieldType> dataProvider;

    private Object draggedItem;
    private Grid<Object> dragSource;

    public KanbanView(Object dataStoreIdentifier,
                      RouteRenderer<?, ?, ?> routeRenderer,
                      VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                       VortexCrudPathToRouteResolver routeResolver,
                      DetailRouteSetting detailRouteSetting
    ) {
        RouteRenderer<ModelClass, FieldType, RepositoryType> typedRouteRenderer =
                (RouteRenderer<ModelClass, FieldType, RepositoryType>) routeRenderer;
        this.routeRenderer = typedRouteRenderer;
        this.context = context;
        this.dataStore = (VortexCrudDataStore<FieldType, Object>) typedRouteRenderer.dataStoreInstance();
        this.fieldNameResolver = context.fieldNameResolver();
        this.reflectionService = context.reflectionService();
        this.dataStoreUtil = context.dataStoreUtil();
        this.routeResolver = routeResolver;

        Selects selects = context.configService().configuration().selects();
        DataStoreConfig<ModelClass, FieldType, RepositoryType> config = typedRouteRenderer.dataStoreConfig();
        this.kanbanRoute = (KanbanRoute<ModelClass, FieldType, RepositoryType>) typedRouteRenderer;
        Field<ModelClass, FieldType, RepositoryType> dataStoreField = config.fields().get(kanbanRoute.columnField());

        this.itemFactory = kanbanRoute.itemFactory();

        dataProvider = new GenericFilterableDataProvider<>(this.dataStore, kanbanRoute.searchField(), typedRouteRenderer.filters());

        itemRenderer = new ComponentRenderer<>(entity -> {
            Div cardWrapper = new Div(itemFactory.renderItem(this.routeRenderer,
                    entity,
                    null,
                    context));
            cardWrapper.getStyle().set("margin", "5px 0");
            cardWrapper.addClickListener(event -> {
                String nextRoute = this.routeResolver.buildPathUpToIndex(this.routeResolver.determineActiveRouteIndex(), dataStoreUtil.getId(entity));
                getUI().ifPresent(ui -> ui.navigate(nextRoute));
            });
            return cardWrapper;
        });

        if (!(dataStoreField instanceof SelectField<ModelClass, FieldType, RepositoryType> selectField)) {
            throw new IllegalStateException("The columnField '" + kanbanRoute.columnField() + "' of kanban route '"
                    + kanbanRoute.title() + "' must be configured as a SelectField in the DataStoreConfig fields map, but was "
                    + (dataStoreField == null ? "not configured at all" : dataStoreField.getClass().getSimpleName())
                    + " — the kanban columns are derived from the select's values");
        }
        String selectName = selectField.values();
        Map<?, String> selectConfig = selects.configs().get(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("Kanban route '" + kanbanRoute.title() + "' uses select config '" + selectName
                    + "' (via columnField '" + kanbanRoute.columnField() + "'), but no select with that key is registered"
                    + " — register it via Application.selects(Selects.builder().configs(Map.of(\"" + selectName + "\", ...)))");
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
        if (this.routeRenderer.actions() != null && !this.routeRenderer.actions().isEmpty()) {
            headerBar.renderActions(this.routeRenderer.actions(), contextConsumer -> {
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
                             VortexCrudPathToRouteResolver routeResolver,
                            Object entity) {
        // Navigate to the entity URL
        if (routeRenderer.form() != null && routeRenderer.form().dialogFactory() != null) {
            Dialog dialog = routeRenderer.form().dialogFactory().create(
                    context.dataStoreUtil().getId(entity),
                    null,
                    null,
                    routeRenderer.form(),
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
                                            reflectionService.getValue(item, kanbanRoute.columnField()),
                                            columnDatabaseValue));
                            if (kanbanRoute.rowIndexField() != null) {
                                stream = stream.sorted(Comparator.comparing(o -> (Comparable) reflectionService.getValue(o, kanbanRoute.rowIndexField())));
                            }
                            return stream.skip(query.getOffset()).limit(query.getLimit());
                        },
                        query -> (int) dataProvider.fetch(new Query<>(0, 1000, Collections.emptyList(), null, query.getFilter().orElse(null)))
                                .filter(item -> Objects.equals(
                                        reflectionService.getValue(item, kanbanRoute.columnField()),
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
            Object sourceColumnValue = reflectionService.getValue(draggedItem, kanbanRoute.columnField());
            // Allow dropping anywhere on the grid, including empty areas — but only on
            // columns the configured workflow permits as transition targets
            columns.forEach((targetColumnValue, g) -> g.setDropMode(
                    isTransitionAllowed(sourceColumnValue, targetColumnValue) ? GridDropMode.BETWEEN : null));
        });

// Replace the grid.addDropListener implementation with this updated version
        grid.addDropListener(event -> {
            if (draggedItem == null || dragSource == null) {
                return;
            }

            // Store original column value
            Object originalColumnValue = reflectionService.getValue(draggedItem, kanbanRoute.columnField());

            // Enforce the configured workflow: reject transitions it does not permit
            if (!isTransitionAllowed(originalColumnValue, columnDatabaseValue)) {
                Notification notification = Notification.show(getTranslation("kanban.transition-not-allowed"));
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            // Update column value
            reflectionService.setValue(draggedItem, kanbanRoute.columnField(), columnDatabaseValue);

            if (kanbanRoute.rowIndexField() != null) {
                // Get fresh items in target column
                List<Object> targetColumnItems = dataStore.getRecordsFromTableWhereColumnEqualsOrdered(
                        kanbanRoute.columnField(),
                        columnDatabaseValue,
                        kanbanRoute.rowIndexField(),
                        0,
                        1000
                );

                Integer newPosition;

                if (event.getDropTargetItem().isEmpty()) {
                    // Dropping at the end
                    Integer lastPosition = targetColumnItems.isEmpty() ? null :
                            rowIndexOf(targetColumnItems.get(targetColumnItems.size() - 1));
                    newPosition = generateKeyBetween(lastPosition, null);
                } else {
                    Object targetItem = event.getDropTargetItem().get();
                    Integer targetPosition = rowIndexOf(targetItem);

                    if (event.getDropLocation() == GridDropLocation.BELOW) {
                        // Find next item's position
                        int targetIndex = targetColumnItems.indexOf(targetItem);
                        Integer nextPosition = (targetIndex < targetColumnItems.size() - 1) ?
                                rowIndexOf(targetColumnItems.get(targetIndex + 1)) : null;
                        newPosition = generateKeyBetween(targetPosition, nextPosition);
                    } else {
                        // Find previous item's position
                        int targetIndex = targetColumnItems.indexOf(targetItem);
                        Integer prevPosition = (targetIndex > 0) ?
                                rowIndexOf(targetColumnItems.get(targetIndex - 1)) : null;
                        newPosition = generateKeyBetween(prevPosition, targetPosition);
                    }
                }

                reflectionService.setValue(draggedItem, kanbanRoute.rowIndexField(), newPosition);
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

    private boolean isTransitionAllowed(Object sourceColumnValue, Object targetColumnValue) {
        return kanbanRoute.workflow() == null
                || kanbanRoute.workflow().isTransitionAllowed(sourceColumnValue, targetColumnValue, context);
    }

    private Integer rowIndexOf(Object item) {
        Object value = reflectionService.getValue(item, kanbanRoute.rowIndexField());
        if (value == null || value instanceof Integer) {
            return (Integer) value;
        }
        throw new IllegalStateException("The rowIndexField '" + kanbanRoute.rowIndexField() + "' of kanban route '"
                + kanbanRoute.title() + "' must contain Integer values but contained "
                + value.getClass().getSimpleName() + " ('" + value + "') — configure an integer column for row ordering");
    }

    private void onAdd(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                       RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer,
                       VortexCrudDataStore<FieldType, ModelClass> dataStore) {
        if (routeRenderer.form() != null && routeRenderer.form().dialogFactory() != null) {
            Dialog dialog = routeRenderer.form().dialogFactory().create(
                    null,
                    null,
                    null,
                    routeRenderer.form(),
                    null,
                    dataStore,
                    context,
                    this::refreshColumns,
                    () -> {
                    });
            dialog.open();
        }
    }
}
