package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.FormItem;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.vaadin.flow.component.grid.Grid;

public class DefaultListColumnImplCallback implements TurboCrudListColumnCallback {

    @Override
    public void addColumn(Grid<GenericEntity> grid, FormItem field, String table, String fieldName, FieldConfig fieldConfig) {
        grid.addColumn(entity -> entity.get(fieldName))
                .setHeader(grid.getTranslation(field.getLabel()))
                .setResizable(true)
                .setAutoWidth(true);
    }
}
