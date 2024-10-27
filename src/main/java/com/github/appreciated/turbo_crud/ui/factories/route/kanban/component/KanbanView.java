package com.github.appreciated.turbo_crud.ui.factories.route.kanban.component;

import com.github.appreciated.turbo_crud.config.model.ApplicationConfig;
import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.KanbanConfig;
import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;
import java.util.Set;

public class KanbanView extends VerticalLayout {

    private final TurboCrudItemFactory itemFactory;
    private final KanbanConfig kanbanConfig;
    private final ComponentRenderer<Component, GenericEntity> itemRenderer;
    private final TurboCrudEntityManagerService entityManagerService;

    public KanbanView(String repository, TurboCrudEntityManagerService entityManagerService, TurboCrudItemFactoryRegistry itemFactoryRegistry, KanbanConfig kanbanConfig, ApplicationConfig configService) {
        this.entityManagerService = entityManagerService;
        ConfigObject selects = configService.getSelects();
        RepositoryConfig config = configService.getRepositoriesConfig().get(repository);
        FieldConfig fieldConfig = config.getFieldsConfig().get(kanbanConfig.getColumnField());

        this.kanbanConfig = kanbanConfig;
        this.itemFactory = itemFactoryRegistry.getFactory(kanbanConfig.getFactory());

        itemRenderer = new ComponentRenderer<>(task -> {
            // Create a component for the card via the TurboCrudItemFactory
            Component card = itemFactory.renderItem(kanbanConfig, task, null);
            // Allow dragging the card
            DragSource<Component> dragSource = DragSource.create(card);
            dragSource.setDragData(task);
            return card;
        });

        String selectName = fieldConfig.getValues();
        ConfigObject selectConfig = selects.toConfig().getObject(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<String> strings = selectConfig.keySet();
        Config translations = selectConfig.toConfig();

        HorizontalLayout kanbanBoard = new HorizontalLayout();
        for (String string : strings) {
            VerticalLayout column = createColumn(getTranslation(translations.getString(string)), string);
            kanbanBoard.add(column);

        }
        kanbanBoard.setSizeFull();
        add(kanbanBoard);
        setSizeFull();
        setPadding(false);
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
                    entityManagerService.updateRecordById(((GenericEntity) o).get("id"), (GenericEntity) o);
                }
            });
        });

        // Add column title
        Div titleLabel = new Div(new H4(title));
        titleLabel.getStyle().set("font-weight", "bold");
        titleLabel.getStyle().set("margin-bottom", "10px");
        wrapper.add(titleLabel);
        wrapper.add(column);

        List<GenericEntity> recordsFromTableWhereColumnEquals = entityManagerService.getRecordsFromTableWhereColumnEquals(kanbanConfig.getColumnField(), columnDatabaseValue, 0, 1000);
        for (GenericEntity record : recordsFromTableWhereColumnEquals) {
            column.add(itemRenderer.createComponent(record));
        }

        return wrapper;
    }
}