package com.github.appreciated.vortex_crud.core.ui.factories.route.view_template;

import com.vaadin.flow.component.Component;

@FunctionalInterface
public interface RouteTemplateProvider<ModelClass, FieldType> {
    Component createLayout(ModelClass item, TemplateBindingContext<ModelClass, FieldType> context);
}
