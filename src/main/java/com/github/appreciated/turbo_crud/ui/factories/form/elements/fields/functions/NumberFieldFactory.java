package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;

import java.util.Collection;
import java.util.List;

public class NumberFieldFactory implements TurboCrudFieldFactory {

    @Override
    public Component createComponent(String table, String field, Field repositoryField) {
        return new NumberField();
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("INTEGER", "INT", "BIGINT", "SMALLINT", "DECIMAL", "NUMERIC");
    }
}
