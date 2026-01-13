package com.github.appreciated.vortex_crud.demo.projectmanagement.ui.view;

import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.TaskStatus;

import java.util.List;

public class DashboardView extends VerticalLayout {

    public DashboardView(JooqDataStore<TaskRecord> taskStore) {
        add(new H1("Project Management Dashboard"));

        add(new H3("Active Tasks"));
        Grid<TaskRecord> taskGrid = new Grid<>();
        taskGrid.addColumn(TaskRecord::getTitle).setHeader("Title");
        taskGrid.addColumn(TaskRecord::getStatus).setHeader("Status");
        taskGrid.addColumn(TaskRecord::getPriority).setHeader("Priority");

        List<TaskRecord> tasks = taskStore.getRecordsFromTable(0, 50);
        List<TaskRecord> activeTasks = tasks.stream()
            .filter(t -> t.getStatus() != TaskStatus.DONE)
            .limit(10)
            .toList();

        taskGrid.setItems(activeTasks);
        add(taskGrid);

        add(new H3("Recent Activity"));
        add("Check the Notifications panel for recent updates.");
    }
}
