package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.select.Select;

import java.util.*;

public class SelectFieldFactory<DataStoreId, FieldId, ModelClass> implements VortexCrudFieldFactory<DataStoreId, FieldId, ModelClass> {

    private final Selects selects;
    private final Map<DataStoreId, DataStoreConfig<DataStoreId, FieldId, ModelClass>> tablesConfig;

    public SelectFieldFactory(Selects selects, Map<DataStoreId, DataStoreConfig<DataStoreId, FieldId, ModelClass>> tablesConfig) {
        this.selects = selects;
        this.tablesConfig = tablesConfig;
    }

    @Override
    public Component createComponent(DataStoreId table, FieldId field, Field<DataStoreId, FieldId, ModelClass> dataStoreField) {
        Select<?> select = new Select<>();

        DataStoreConfig<DataStoreId, FieldId, ModelClass> dataStoreConfig = tablesConfig.get(table);
        Field<DataStoreId, FieldId, ModelClass> tableField = dataStoreConfig.getFields().get(field);

        String selectName = tableField.getValues();
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
