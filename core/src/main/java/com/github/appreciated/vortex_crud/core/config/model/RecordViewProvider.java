package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.view.StoreAccessor;
import com.vaadin.flow.component.Component;

@FunctionalInterface
public interface RecordViewProvider<ModelClass, FieldType, RepositoryType> {
    Component createView(ModelClass record, StoreAccessor<ModelClass, FieldType, RepositoryType> store);
}
