package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.config.model.FormField;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.grid.Grid;

public class DefaultListColumnImplCallback implements TurboCrudListColumnCallback {

    @Override
    public void addColumn(Grid<GenericEntity> grid, FormField field, String table, String fieldName, FieldConfig fieldConfig) {
        grid.addColumn(entity -> entity.get(fieldName))
                .setHeader(grid.getTranslation(field.getLabel()))
                .setResizable(true)
                .setAutoWidth(true);
    }
}
