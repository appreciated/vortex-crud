package com.github.appreciated.vortex_crud.core.ui.factories.route.view;

import com.vaadin.flow.component.Component;

@FunctionalInterface
public interface CustomViewFactory<ModelClass> {
    Component create(ModelClass record);
}
