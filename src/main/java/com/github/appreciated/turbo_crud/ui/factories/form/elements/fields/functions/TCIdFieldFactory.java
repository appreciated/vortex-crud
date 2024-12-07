package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Collection;
import java.util.List;

public class TCIdFieldFactory extends TCTextFieldFactory{

    @Override
    public Component createComponent(String table, String field, Field repositoryField) {
        TextField textField = new TextField();
        textField.setReadOnly(true);
        return textField;
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("UUID", "INTEGER", "INT", "CHAR", "VARCHAR", "SERIAL");
    }
}
