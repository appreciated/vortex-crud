package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions;

import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.TurboCrudFieldFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.select.Select;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class DefaultSelectFactory implements TurboCrudFieldFactory {

    private final ConfigObject selectsConfig;
    private final Map<String, RepositoryConfig> tablesConfig;

    public DefaultSelectFactory(ConfigObject selectsConfig, Map<String, RepositoryConfig> tablesConfig) {
        this.selectsConfig = selectsConfig;
        this.tablesConfig = tablesConfig;
    }

    @Override
    public Component createComponent(String table, String field, FieldConfig fieldConfig) {
        Select<String> select = new Select<>();

        RepositoryConfig repositoryConfig = tablesConfig.get(table);
        FieldConfig tableFieldConfig = repositoryConfig.getFieldsConfig().get(field);

        String selectName = tableFieldConfig.getValues();
        ConfigObject selectConfig = selectsConfig.toConfig().getObject(selectName);

        if (selectConfig == null) {
            throw new IllegalStateException("selectConfig must not be null");
        }

        Set<String> strings = selectConfig.keySet();
        Config config = selectConfig.toConfig();
        select.setItems(new ArrayList<>(strings));
        select.setItemLabelGenerator(item -> select.getTranslation(config.getString(item)));

        return select;
    }
}
