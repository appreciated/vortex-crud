package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component.EntityComboBoxWrapper;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class ReferenceFieldFactory implements TurboCrudFieldFactory {

    private final TurboCrudDataStoreFactoryRegistry managerFactoryRegistry;

    public ReferenceFieldFactory(TurboCrudDataStoreFactoryRegistry managerFactoryRegistry) {
        this.managerFactoryRegistry = managerFactoryRegistry;
    }

    @Override
    public Component createComponent(String table, String field, Field repositoryField) {
        return new EntityComboBoxWrapper(managerFactoryRegistry, repositoryField);
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
       return List.of("UUID", "INTEGER", "INT", "CHAR", "VARCHAR", "SERIAL");
    }
}