package com.github.appreciated.turbo_crud.ui.factories.route.kanban.component;

import com.github.appreciated.turbo_crud.config.model.KanbanConfig;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class KanbanView extends VerticalLayout {

    private final TurboCrudItemFactory itemFactory;
    private final KanbanConfig kanbanConfig;

    public KanbanView(TurboCrudItemFactoryRegistry itemFactoryRegistry, KanbanConfig kanbanConfig) {
        this.kanbanConfig = kanbanConfig;
        this.itemFactory = itemFactoryRegistry.getFactory(kanbanConfig.getFactory());

        // Erstelle die drei Spalten
        FlexLayout todoColumn = createColumn("To Do");
        FlexLayout inProgressColumn = createColumn("In Progress");
        FlexLayout doneColumn = createColumn("Done");

        // Beispielhafte Aufgaben hinzufügen
        todoColumn.add(createCardComponent(new GenericEntity()));
        todoColumn.add(createCardComponent(new GenericEntity()));
        todoColumn.add(createCardComponent(new GenericEntity()));

        // Layout für die Kanban-Tafel
        HorizontalLayout kanbanBoard = new HorizontalLayout(todoColumn, inProgressColumn, doneColumn);
        kanbanBoard.setSizeFull();
        add(kanbanBoard);
        setSizeFull();
    }

    // Methode zur Erstellung einer Spalte
    private FlexLayout createColumn(String title) {
        FlexLayout column = new FlexLayout();
        column.setWidth("300px");
        column.setHeightFull();
        column.getStyle().set("border", "1px solid lightgray");
        column.getStyle().set("flex-direction", "column");

        // Drop-Ziele für Drag-and-Drop ermöglichen
        DropTarget<FlexLayout> dropTarget = DropTarget.create(column);
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
            card.getElement().getStyle().set("padding", "10px");
            card.getElement().getStyle().set("border", "1px solid black");
            card.getElement().getStyle().set("background-color", "white");
            card.getElement().getStyle().set("margin-bottom", "10px");

            // Ermögliche das Draggen der Karte
            DragSource<Component> dragSource = DragSource.create(card);
            dragSource.setDragData(task);

            return card;
        });

        // Karte dynamisch mit TurboCrudItemFactory rendern und zurückgeben
        return taskRenderer.createComponent(genericEntity);
    }
}