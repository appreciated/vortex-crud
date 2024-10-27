package com.github.appreciated.turbo_crud.ui.factories.route.kanban.component;

import com.github.appreciated.turbo_crud.config.model.ApplicationConfig;
import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.KanbanConfig;
import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KanbanView extends VerticalLayout {

    private final TurboCrudItemFactory itemFactory;
    private final KanbanConfig kanbanConfig;

    public KanbanView(String repository, TurboCrudEntityManagerService entityManagerService, TurboCrudItemFactoryRegistry itemFactoryRegistry, KanbanConfig kanbanConfig, ApplicationConfig configService) {
        ConfigObject selects = configService.getSelects();
        RepositoryConfig config = configService.getRepositoriesConfig().get(repository);
        FieldConfig fieldConfig = config.getFieldsConfig().get(kanbanConfig.getColumnField());

        this.kanbanConfig = kanbanConfig;
        this.itemFactory = itemFactoryRegistry.getFactory(kanbanConfig.getFactory());

        String selectName = fieldConfig.getValues();
        ConfigObject selectConfig = selects.toConfig().getObject(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<String> strings = selectConfig.keySet();
        Config translations = selectConfig.toConfig();

        HorizontalLayout kanbanBoard = new HorizontalLayout();
        for (String string : strings) {
            VerticalLayout column = createColumn(getTranslation(translations.getString(string)));
            kanbanBoard.add(column);
            List<GenericEntity> recordsFromTableWhereColumnEquals = entityManagerService.getRecordsFromTableWhereColumnEquals(kanbanConfig.getColumnField(), string, 0, 1000);
            for (GenericEntity record : recordsFromTableWhereColumnEquals) {
                column.add(createCardComponent(record));
            }
        }
        kanbanBoard.setSizeFull();
        add(kanbanBoard);
        setSizeFull();
    }

    // Methode zur Erstellung einer Spalte
    private VerticalLayout createColumn(String title) {
        VerticalLayout column = new VerticalLayout();
        column.setWidth("300px");
        column.setHeightFull();
        column.addClassNames("card", "no-hover");

        // Drop-Ziele für Drag-and-Drop ermöglichen
        DropTarget<VerticalLayout> dropTarget = DropTarget.create(column);
        dropTarget.addDropListener(event -> {
            Component draggedComponent = event.getDragSourceComponent().orElse(null);
            if (draggedComponent != null) {
                column.add(draggedComponent);
            }
        });

        // Titel der Spalte hinzufügen
        Div titleLabel = new Div(new Text(title));
        titleLabel.getStyle().set("font-weight", "bold");
        titleLabel.getStyle().set("margin-bottom", "10px");
        column.add(titleLabel);

        return column;
    }

    // Methode zur Erstellung einer Karte (Aufgabe) mit TurboCrudItemFactory
    private Component createCardComponent(GenericEntity genericEntity) {
        ComponentRenderer<Component, GenericEntity> taskRenderer = new ComponentRenderer<>(task -> {
            // Erzeuge eine Komponente für die Karte über die TurboCrudItemFactory
            Component card = itemFactory.renderItem(kanbanConfig, genericEntity, null);

            // Ermögliche das Draggen der Karte
            DragSource<Component> dragSource = DragSource.create(card);
            dragSource.setDragData(task);

            return card;
        });

        // Karte dynamisch mit TurboCrudItemFactory rendern und zurückgeben
        return taskRenderer.createComponent(genericEntity);
    }
}