package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Collection;
import java.util.List;

public class IdFieldFactory<DataStoreId, FieldId, ModelClass>  extends TextFieldFactory<DataStoreId, FieldId, ModelClass>  {

     @Override
    public Component createComponent(DataStoreId table, FieldId field, Field<DataStoreId, FieldId, ModelClass>  dataStoreField) {
        TextField textField = new TextField();
        textField.setReadOnly(true);
        return textField;
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("UUID", "INTEGER", "INT", "CHAR", "VARCHAR", "SERIAL");
    }
}
