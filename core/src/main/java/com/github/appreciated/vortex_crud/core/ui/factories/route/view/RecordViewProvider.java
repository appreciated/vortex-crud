package com.github.appreciated.vortex_crud.core.ui.factories.route.view;

import com.vaadin.flow.component.Component;

@FunctionalInterface
public interface RecordViewProvider<ModelClass, FieldType, RepositoryType> {
    Component createView(ModelClass record, StoreAccessor<ModelClass, FieldType, RepositoryType> store);
}
