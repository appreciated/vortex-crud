package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;

@FunctionalInterface
public interface RecordViewProvider<ModelClass> {
    Component createView(ModelClass record);
}
