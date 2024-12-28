package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.Collection;
import java.util.List;

public class TextAreaFieldFactory<DataStoreId, FieldId> implements TurboCrudFieldFactory<DataStoreId, FieldId> {

    @Override
    public Component createComponent(DataStoreId table, FieldId field, Field dataStoreField) {
        return new TextArea();
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING", "CHAR", "TEXT", "CLOB");
    }
}
