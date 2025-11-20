package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.config.model.fields.MultiSelectValueField;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.CheckboxGroup;

import java.util.*;

public class MultiSelectValueFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    private final Selects selects;

    public MultiSelectValueFieldFactory(Selects selects, Map<RepositoryType, ?> tablesConfig) {
        this.selects = selects;
    }

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField) {
        CheckboxGroup<?> checkboxGroup = new CheckboxGroup<>();

        MultiSelectValueField<ModelClass, FieldType, RepositoryType> msf = (MultiSelectValueField<ModelClass, FieldType, RepositoryType>) dataStoreField;
        String selectName = msf.values();
        Map<?, String> selectConfig = selects.configs().get(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<?> keySet = selectConfig.keySet();
        checkboxGroup.setItems(new ArrayList(keySet));
        checkboxGroup.setItemLabelGenerator(item -> checkboxGroup.getTranslation(selectConfig.get(item)));

        return checkboxGroup;
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING", "TEXT", "ARRAY");
    }
}
