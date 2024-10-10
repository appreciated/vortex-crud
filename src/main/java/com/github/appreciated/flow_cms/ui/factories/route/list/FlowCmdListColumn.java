package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.FieldConfig;
import com.github.appreciated.flow_cms.config.model.FormField;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.factories.fields.FlowCmsFieldFactory;
import com.vaadin.flow.component.grid.Grid;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface FlowCmdListColumn {
    void addColumn(Grid<GenericEntity> grid, FormField field, String table, String fieldName, FieldConfig fieldConfig);
}
