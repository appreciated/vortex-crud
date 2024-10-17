package com.github.appreciated.turbo_crud.ui.factories.route.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class KanbanView extends VerticalLayout {

    public KanbanView() {
        // Erstelle die drei Spalten
        FlexLayout todoColumn = createColumn("To Do");
        FlexLayout inProgressColumn = createColumn("In Progress");
        FlexLayout doneColumn = createColumn("Done");

        // Füge einige Aufgaben (Cards) zur "To Do" Spalte hinzu
        todoColumn.add(createCard("Task 1"));
        todoColumn.add(createCard("Task 2"));
        todoColumn.add(createCard("Task 3"));

        // Layout für die Kanban-Tafel
        HorizontalLayout kanbanBoard = new HorizontalLayout(todoColumn, inProgressColumn, doneColumn);
        kanbanBoard.setSizeFull();
        add(kanbanBoard);
    }

    // Methode zur Erstellung einer Spalte
    private FlexLayout createColumn(String title) {
        FlexLayout column = new FlexLayout();
        column.setWidth("300px");
        column.setHeightFull();
        column.getStyle().set("border", "1px solid lightgray");
        column.getStyle().set("flex-direction", "column");
        
        // Drop-Ziele definieren, um Drag-and-Drop zu ermöglichen
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

    // Methode zur Erstellung einer Karte (Aufgabe)
    private Component createCard(String taskName) {
        Div card = new Div(new Text(taskName));
        card.getStyle().set("padding", "10px");
        card.getStyle().set("border", "1px solid black");
        card.getStyle().set("background-color", "white");
        card.getStyle().set("margin-bottom", "10px");
        card.setWidthFull();

        // Ermögliche das Draggen der Karte
        DragSource<Div> dragSource = DragSource.create(card);
        dragSource.setDragData(taskName);

        return card;
    }
}
