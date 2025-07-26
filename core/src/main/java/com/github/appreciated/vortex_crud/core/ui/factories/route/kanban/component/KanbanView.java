package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
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
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

        itemRenderer = new ComponentRenderer<>(entity -> {
            // Create a component for the card via the VortexCrudItemFactory
            Div cardWrapper = new Div(itemFactory.renderItem(kanbanConfig,
                    entity,
                    null,
                    fileProviderRegistry,
                    fieldNameResolver,
                    reflectionService));
            // Allow dragging the card
            DragSource<Component> dragSource = DragSource.create(cardWrapper);
            dragSource.setDragData(entity);
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
            VerticalLayout column = createColumn(getTranslation(selectConfig.get(string)), string);
            kanbanBoard.add(column);
        }
        kanbanBoard.setSizeFull();

        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, routeRenderer, dataStoreIdentifier, formCreator, routeFactory),
                null,
                null,
                routeHeader);

        if (!detailRouteSetting.isHeaderHidden()) {
            add(headerBar);
        }

        add(kanbanBoard);
        setSizeFull();
        setPadding(true);
    }

    private void openDialog(KeyType dataStoreIdentifier, RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer, VortexCrudDataStore<FieldId, ?> dataStore, VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory, Kanban<DataStoreId, FieldId, KeyType> kanbanConfig, VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry, VortexCrudFileProviderRegistry fileProviderRegistry, VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver, FormCreator<DataStoreId, FieldId, KeyType> formCreator, ReflectionService<FieldId> reflectionService, VortexCrudDataStoreUtilStrategy dataStoreUtil, VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver, Object entity, Div cardWrapper) {
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
                    //TODO handle if the column was edited, requiring the element to move
                    Object recordById = dataStore.getRecordById(dataStoreUtil.getId(entity));
                    cardWrapper.removeAll();
                    cardWrapper.add(itemFactory.renderItem(kanbanConfig,
                            recordById,
                            null,
                            fileProviderRegistry,
                            fieldNameResolver,
                            reflectionService));
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
                    recordById,
                    null
            );
        }
    }

    private VerticalLayout createColumn(String title, Object columnDatabaseValue) {
        VerticalLayout column = new VerticalLayout();
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setHeightFull();
        wrapper.setWidth("300px");
        wrapper.getStyle().set("flex", "0 0 auto")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("border-radius", "var(--lumo-border-radius-l)");
        wrapper.addClassNames("no-hover");
        wrapper.setSpacing(false);
        column.setPadding(false);

        // Enable drag and drop and drop targets
        DropTarget<VerticalLayout> dropTarget = DropTarget.create(column);
        dropTarget.addDropListener(event -> {
            Component draggedComponent = event.getDragSourceComponent().orElse(null);
            if (draggedComponent != null) {
                column.add(draggedComponent);
            }
            event.getDragData().ifPresent(o -> {
                if (o instanceof Object) {
                    reflectionService.setValue(o, kanbanConfig.getColumnField(), columnDatabaseValue);
                    dataStore.updateRecordById(o);
                }
            });
        });

        // Add column title
        Div titleLabel = new Div(new H4(title));
        titleLabel.getStyle().set("font-weight", "bold");
        titleLabel.getStyle().set("margin-bottom", "10px");
        wrapper.add(titleLabel);
        wrapper.add(column);

        List<?> recordsFromTableWhereColumnEquals = dataStore.getRecordsFromTableWhereColumnEquals(kanbanConfig.getColumnField(), columnDatabaseValue, 0, 1000);
        for (Object record : recordsFromTableWhereColumnEquals) {
            column.add(itemRenderer.createComponent(record));
        }

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
                    itemFactory.renderItem(kanbanConfig, recordById, null, fileProviderRegistry, fieldNameResolver, reflectionService);
                },
                () -> {

                },
                formCreator);
        dialog.open();
    }
}
