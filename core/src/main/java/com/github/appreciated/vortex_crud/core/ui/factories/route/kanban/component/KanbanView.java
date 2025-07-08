package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
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
import com.vaadin.flow.component.Component;
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
import java.util.Set;

public class KanbanView<DataStoreId, FieldId> extends VerticalLayout {

    private final VortexCrudItemFactory<FieldId> itemFactory;
    private final Kanban<DataStoreId, FieldId> kanbanConfig;
    private final ComponentRenderer<Component, Object> itemRenderer;
    private final VortexCrudDataStore<FieldId, ?> dataStore;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final ReflectionService reflectionService;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;

    public KanbanView(DataStoreId dataStoreIdentifier,
                      RouteRenderer<DataStoreId, FieldId> routeRenderer,
                      VortexCrudDataStore<FieldId, ?> dataStore,
                      VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                      VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                      Kanban<DataStoreId, FieldId> kanbanConfig,
                      Application<DataStoreId, FieldId> configService,
                      VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
                      VortexCrudFileProviderRegistry fileProviderRegistry,
                      VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                      FormCreator<DataStoreId, FieldId> formCreator,
                      DetailRouteSetting detailRouteSetting,
                      ReflectionService reflectionService
                      ) {
        this.dataStore = dataStore;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
        Selects selects = configService.getSelects();
        DataStoreConfig<DataStoreId, FieldId> config = configService.getDataStores().get(dataStoreIdentifier);
        Field<DataStoreId, FieldId> dataStoreField = config.getFields().get(kanbanConfig.getColumnField());

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
                Dialog dialog = dialogFactoryRegistry.getFactory(routeRenderer.getChild().getFactory()).create(
                        DataStoreUtil.getId(entity),
                        null,
                        null,
                        routeRenderer.getChild(),
                        null,
                        dataStoreIdentifier,
                        routeFactory,
                        () -> {
                            //TODO handle if the column was edited, requiring the element to move
                            Object recordById = dataStore.getRecordById(DataStoreUtil.getId(entity));
                            cardWrapper.removeAll();
                            cardWrapper.add(itemFactory.renderItem(kanbanConfig,
                                    recordById,
                                    null,
                                    fileProviderRegistry,
                                    fieldNameResolver,
                                    reflectionService));
                        },
                        formCreator);
                dialog.open();
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
                    ((Object) o).put(fieldNameResolver.getKeyForFieldId(kanbanConfig.getColumnField()), columnDatabaseValue);
                    dataStore.updateRecordById(((Object) o).get("id"), (Object) o);
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

    private void onAdd(VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry, RouteRenderer<DataStoreId, FieldId> routeRenderer, DataStoreId dataStore, FormCreator<DataStoreId, FieldId> formCreator, VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory) {
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
                    Object recordById = this.dataStore.getRecordById(DataStoreUtil.getId(entity));
                    itemFactory.renderItem(kanbanConfig, recordById, null, fileProviderRegistry, fieldNameResolver, reflectionService);
                },
                formCreator);
        dialog.open();
    }
}