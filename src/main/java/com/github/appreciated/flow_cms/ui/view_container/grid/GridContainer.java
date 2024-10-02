package com.github.appreciated.flow_cms.ui.view_container.grid;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.List;

public class GridContainer extends Grid<GenericEntity> {

    public GridContainer(int i, ConfigObject config, DynamicEntityManagerService entityManagerService) {
        String table = config.get("table").render();

        // Virtual List mit Lazy Loading einrichten
        DataProvider<GenericEntity, Void> dataProvider = new CallbackDataProvider<>(
                query -> {
                    List<GenericEntity> items = entityManagerService.getRecordsFromTable(table, query.getOffset(), query.getLimit());
                    return items.stream();
                },
                query -> entityManagerService.count(table)
        );

        setDataProvider(dataProvider);
    }
}
