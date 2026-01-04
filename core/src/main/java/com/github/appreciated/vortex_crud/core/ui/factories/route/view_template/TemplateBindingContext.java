package com.github.appreciated.vortex_crud.core.ui.factories.route.view_template;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;

public interface TemplateBindingContext<ModelClass, FieldType> {
    Component getComponent(FieldType field);

    Binder<ModelClass> getBinder();

    void triggerSave();

    Component getActionButtons();

    Component getHeader();
}
