package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.FormElement;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.vaadin.flow.component.grid.Grid;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface TurboCrudListColumnCallback {
    void addColumn(Grid<GenericEntity> grid, FormElement field, String table, String fieldName, FieldConfig fieldConfig);
}
