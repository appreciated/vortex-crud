package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Collection;
import java.util.List;

public class IdFieldFactory<DataStoreId, FieldId> extends TextFieldFactory<DataStoreId, FieldId> {

    @Override
    public Component createComponent(DataStoreId table, FieldId field, Field dataStoreField) {
        TextField textField = new TextField();
        textField.setReadOnly(true);
        return textField;
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("UUID", "INTEGER", "INT", "CHAR", "VARCHAR", "SERIAL");
    }
}
