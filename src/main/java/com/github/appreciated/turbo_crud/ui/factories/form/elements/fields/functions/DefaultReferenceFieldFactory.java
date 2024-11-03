package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component.EntityComboBoxWrapper;
import com.vaadin.flow.component.Component;

public class DefaultReferenceFieldFactory implements TurboCrudFieldFactory {

    private final TurboCrudEntityManagerFactoryRegistry managerFactoryRegistry;

    public DefaultReferenceFieldFactory(TurboCrudEntityManagerFactoryRegistry managerFactoryRegistry) {
        this.managerFactoryRegistry = managerFactoryRegistry;
    }

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        return new EntityComboBoxWrapper(managerFactoryRegistry, fieldConfig);
    }
}