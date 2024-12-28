package com.github.appreciated.turbo_crud.core.ui.factories.route.kanban.component;

import com.github.appreciated.turbo_crud.core.config.model.*;
import com.github.appreciated.turbo_crud.core.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
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

public class KanbanView extends VerticalLayout {

    private final TurboCrudItemFactory itemFactory;
    private final Kanban kanbanConfig;
    private final ComponentRenderer<Component, GenericEntity> itemRenderer;
    private final TurboCrudDataStore dataStore;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;

    public <DataStoreId, FieldId> KanbanView(DataStoreId dataStoreIdentifier,
                          Route<DataStoreId, FieldId> route,
                          TurboCrudDataStore dataStore,
                          TurboCrudRouteFactoryRegistry routeFactory,
                          TurboCrudItemFactoryRegistry itemFactoryRegistry,
                          Kanban<DataStoreId, FieldId> kanbanConfig,
                          Application<DataStoreId, FieldId> configService,
                          TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                          TurboCrudFileProviderRegistry fileProviderRegistry,
                          FormCreator formCreator,
                          DetailRouteSetting detailRouteSetting) {
        this.dataStore = dataStore;
        Selects selects = configService.getSelects();
        DataStoreConfig<?> config = configService.getDataStores().get(dataStoreIdentifier);
        Field dataStoreField = config.getFields().get(kanbanConfig.getColumnField());

        this.kanbanConfig = kanbanConfig;
        this.itemFactory = itemFactoryRegistry.getFactory(kanbanConfig.getFactory());
        this.fileProviderRegistry = fileProviderRegistry;

        itemRenderer = new ComponentRenderer<>(entity -> {
            // Create a component for the card via the TurboCrudItemFactory
            Div cardWrapper = new Div(itemFactory.renderItem(kanbanConfig, entity, null, fileProviderRegistry));
            // Allow dragging the card
            DragSource<Component> dragSource = DragSource.create(cardWrapper);
            dragSource.setDragData(entity);
            cardWrapper.addClickListener(event -> {
                Dialog dialog = dialogFactoryRegistry.getFactory(route.getChild().getFactory()).create(
                        DataStoreUtil.getId(entity),
                        null,
                        null,
                        route.getChild(),
                        null,
                        dataStoreIdentifier,
                        routeFactory,
                        () -> {
                            //TODO handle if the column was edited, requiring the element to move
                            GenericEntity recordById = dataStore.getRecordById(DataStoreUtil.getId(entity));
                            cardWrapper.removeAll();
                            cardWrapper.add(itemFactory.renderItem(kanbanConfig, recordById, null, fileProviderRegistry));
                        },
                        formCreator);
                dialog.open();
            });
            return cardWrapper;
        });

        String selectName = dataStoreField.getValues();
        Map<String, String> selectConfig = selects.getConfigs().get(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<String> strings = selectConfig.keySet();

        HorizontalLayout kanbanBoard = new HorizontalLayout();

        kanbanBoard.getStyle()
                .set("flex", "1 1 auto")
                .set("overflow", "auto");

        for (String string : strings) {
            VerticalLayout column = createColumn(getTranslation(selectConfig.get(string)), string);
            kanbanBoard.add(column);
        }
        kanbanBoard.setSizeFull();

        RouteHeader routeHeader = new RouteHeader(route);
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, route, dataStoreIdentifier, formCreator, routeFactory),
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

    private VerticalLayout createColumn(String title, String columnDatabaseValue) {
        VerticalLayout column = new VerticalLayout();
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setHeightFull();
        wrapper.setWidth("300px");
        wrapper.getStyle().set("overflow", "hidden");
        wrapper.getStyle().set("flex", "0 0 auto");
        wrapper.addClassNames("card", "no-hover");
        wrapper.setSpacing(false);
        column.setPadding(false);
        column.setHeightFull();
        column.getStyle().set("overflow", "auto");

        // Enable drag and drop and drop targets
        DropTarget<VerticalLayout> dropTarget = DropTarget.create(column);
        dropTarget.addDropListener(event -> {
            Component draggedComponent = event.getDragSourceComponent().orElse(null);
            if (draggedComponent != null) {
                column.add(draggedComponent);
            }
            event.getDragData().ifPresent(o -> {
                if (o instanceof GenericEntity) {
                    ((GenericEntity) o).put(kanbanConfig.getColumnField(), columnDatabaseValue);
                    dataStore.updateRecordById(((GenericEntity) o).get("id"), (GenericEntity) o);
                }
            });
        });

        // Add column title
        Div titleLabel = new Div(new H4(title));
        titleLabel.getStyle().set("font-weight", "bold");
        titleLabel.getStyle().set("margin-bottom", "10px");
        wrapper.add(titleLabel);
        wrapper.add(column);

        List<GenericEntity> recordsFromTableWhereColumnEquals = dataStore.getRecordsFromTableWhereColumnEquals(kanbanConfig.getColumnField(), columnDatabaseValue, 0, 1000);
        for (GenericEntity record : recordsFromTableWhereColumnEquals) {
            column.add(itemRenderer.createComponent(record));
        }

        return wrapper;
    }

    private <DataStoreId, FieldId> void onAdd(TurboCrudDialogFactoryRegistry dialogFactoryRegistry, Route<DataStoreId, FieldId> route, DataStoreId dataStore, FormCreator formCreator, TurboCrudRouteFactoryRegistry routeFactory) {
        GenericEntity entity = new GenericEntity();
        Dialog dialog = dialogFactoryRegistry.getFactory(route.getChild().getFactory()).create(
                null,
                null,
                null,
                route.getChild(),
                null,
                dataStore,
                routeFactory,
                () -> {
                    GenericEntity recordById = this.dataStore.getRecordById(DataStoreUtil.getId(entity));
                    itemFactory.renderItem(kanbanConfig, recordById, null, fileProviderRegistry);
                },
                formCreator);
        dialog.open();
    }
}