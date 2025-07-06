package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;

import java.util.Collection;
import java.util.List;

public class CheckboxFieldFactory<DataStoreId, FieldId, ModelClass>  implements VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass>  {

    @Override
    public Component createComponent(DataStoreId table, FieldId field, Field<DataStoreId, FieldId, ModelClass>  dataStoreField) {
        return new Checkbox();
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("BOOLEAN", "BIT");
    }
}