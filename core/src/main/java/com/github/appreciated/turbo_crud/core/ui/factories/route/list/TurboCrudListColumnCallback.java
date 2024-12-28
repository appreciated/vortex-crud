package com.github.appreciated.turbo_crud.core.ui.factories.route.list;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.config.model.InternalFormElement;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.vaadin.flow.component.grid.Grid;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface TurboCrudListColumnCallback {
    void addColumn(Grid<GenericEntity> grid, InternalFormElement field, Object table, String fieldName, Field dataStoreField);
}
