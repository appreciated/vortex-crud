package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.DataStore;
import com.github.appreciated.turbo_crud.config.model.Field;
import com.github.appreciated.turbo_crud.config.model.Selects;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.select.Select;

import java.util.*;

public class SelectFieldFactory implements TurboCrudFieldFactory {

    private final Selects selects;
    private final Map<String, DataStore> tablesConfig;

    public SelectFieldFactory(Selects selects, Map<String, DataStore> tablesConfig) {
        this.selects = selects;
        this.tablesConfig = tablesConfig;
    }

    @Override
    public Component createComponent(String table, String field, Field dataStoreField) {
        Select<String> select = new Select<>();

        DataStore dataStore = tablesConfig.get(table);
        Field tableField = dataStore.getFields().get(field);

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
