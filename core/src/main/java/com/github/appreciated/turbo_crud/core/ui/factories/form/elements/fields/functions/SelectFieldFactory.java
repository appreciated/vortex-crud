package com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.turbo_crud.core.config.model.Field;
import com.github.appreciated.turbo_crud.core.config.model.Selects;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.select.Select;

import java.util.*;

public class SelectFieldFactory<DataStoreId, FieldId> implements TurboCrudFieldFactory<DataStoreId, FieldId> {

    private final Selects selects;
    private final Map<DataStoreId, DataStoreConfig<DataStoreId, FieldId>> tablesConfig;

    public SelectFieldFactory(Selects selects, Map<DataStoreId, DataStoreConfig<DataStoreId, FieldId>> tablesConfig) {
        this.selects = selects;
        this.tablesConfig = tablesConfig;
    }

    @Override
    public Component createComponent(DataStoreId table, FieldId field, Field<DataStoreId, FieldId> dataStoreField) {
        Select<String> select = new Select<>();

        DataStoreConfig<DataStoreId, FieldId> dataStoreConfig = tablesConfig.get(table);
        Field<DataStoreId, FieldId> tableField = dataStoreConfig.getFields().get(field);

        String selectName = tableField.getValues();
        Map<String, String> selectConfig = selects.getConfigs().get(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<String> strings = selectConfig.keySet();
        select.setItems(new ArrayList<>(strings));
        select.setItemLabelGenerator(item -> select.getTranslation(selectConfig.get(item)));

        return select;
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("VARCHAR", "CHARACTER VARYING");
    }
}
