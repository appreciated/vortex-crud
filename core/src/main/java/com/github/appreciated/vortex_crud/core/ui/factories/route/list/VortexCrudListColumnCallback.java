package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.vaadin.flow.component.grid.Grid;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface VortexCrudListColumnCallback<DataStoreId, FieldId, ModelClass> {
    void addColumn(Grid<ModelClass> grid, InternalFormElement<DataStoreId, FieldId, ModelClass> field, Object table, String fieldName, Field<DataStoreId, FieldId, ModelClass> dataStoreField);
}
