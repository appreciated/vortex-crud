package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.select.Select;

import java.util.*;

public class SelectFieldFactory<DataStoreId, FieldId, KeyType> implements VortexCrudFieldFactory<DataStoreId, FieldId, KeyType> {

    private final Selects selects;

    public SelectFieldFactory(Selects selects, Map<KeyType, ?> tablesConfig) {
        this.selects = selects;
    }

    @Override
    public Component createComponent(KeyType table, FieldId field, Field<DataStoreId, FieldId, KeyType> dataStoreField) {
        Select<?> select = new Select<>();

        SelectField<DataStoreId, FieldId, KeyType> sf = (SelectField<DataStoreId, FieldId, KeyType>) dataStoreField;
        String selectName = sf.getValues();
        Map<?, String> selectConfig = selects.getConfigs().get(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<?> strings = selectConfig.keySet();
        select.setItems(new ArrayList(strings));
        select.setItemLabelGenerator(item -> select.getTranslation(selectConfig.get(item)));

        return select;
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING");
    }
}
