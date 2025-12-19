package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.annotation.NoCoverage;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Collection;
import java.util.List;

/**
 * Factory for string-based ID fields (UUID, etc.).
 * Creates a read-only TextField for displaying string ID values.
 */
public class StringIdFieldFactory<ModelClass, FieldType, RepositoryType> extends TextFieldFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField, VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        TextField textField = new TextField();
        textField.setReadOnly(true);
        return textField;
    }

    @NoCoverage
    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("UUID", "CHAR", "VARCHAR", "TEXT");
    }
}
